package ro.pds.PaperDisseminationSystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.pds.PaperDisseminationSystem.entities.*;
import ro.pds.PaperDisseminationSystem.exceptions.*;
import ro.pds.PaperDisseminationSystem.security.jwt.JwtUtils;
import ro.pds.PaperDisseminationSystem.services.*;
import ro.pds.PaperDisseminationSystem.view.request.TestDto;
import ro.pds.PaperDisseminationSystem.view.response.TestScoreDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("api/userTests")
public class UserTestController {
    @Autowired
    private UserTestService userTestService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TagLevelService tagLevelService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private TestQuestionService testQuestionService;

    @Autowired
    private TestAnswerService testAnswerService;

    @GetMapping("")
    public ResponseEntity<?> getLatestUserTest(@RequestParam Long userId, @RequestParam Long tagId) {
        try {
            User user = userService.getUserById(userId);
            Tag tag = tagService.getTagById(tagId);
            UserTest userTest = userTestService.getLatestUserTest(userId, tagId);
            if(userTest == null){
                return new ResponseEntity<>("Could not identify any test for the user " + user.getName() + " , for the test " + tag.getName(), HttpStatus.NOT_FOUND);
            }
            TestScoreDto testScoreDto = new TestScoreDto(null, null, userTest.getTagLevel().getCID());
            return new ResponseEntity<>(testScoreDto, HttpStatus.OK);
        } catch (UserNotFound | TagNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> computeTestScore(@Valid @RequestBody TestDto testDto,
                                              @RequestHeader(name = "Authorization") String token) {
        User user;
        try {
            user = getUser(token);
        } catch (UserNotFound e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Long tagId = testDto.getTagId();
            tagService.getTagById(tagId);
            UserTest latestUserTest = userTestService.getLatestUserTest(user.getId(), tagId);

            if (latestUserTest == null || latestUserTest.getCreatedAt().isBefore(LocalDateTime.now().minusMonths(3))) {
                Integer score = computeTestScore(testDto);
                Integer maxScore = testQuestionService.listAllQuestionsByTagId(tagId).size();
                String CID = getCIDForUserLevel(tagId, score, maxScore, user);
                TestScoreDto testScoreDto = new TestScoreDto(score, maxScore, CID);
                return new ResponseEntity<>(testScoreDto, HttpStatus.OK);
            } else {
                if (latestUserTest.getReceivedBadge() || latestUserTest.getTagLevel() == null) {
                    return new ResponseEntity<>("Sorry, you have attempted the test too recently. Please wait at least 3 months before trying again.", HttpStatus.FORBIDDEN);
                } else{
                    return new ResponseEntity<>("You have already attempted this test. Please obtain your badge.", HttpStatus.FORBIDDEN);
                }
            }
        } catch (TagNotFound | TestQuestionNotFound | TestAnswerNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch(InvalidScore e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private Integer computeTestScore(TestDto testDto) throws TestAnswerNotFound, TestQuestionNotFound {
        Integer score = 0;
        Set<Long> qIds = new HashSet<>();
        for (Long answerId : testDto.getAnswerIds()) {
            TestAnswer answer = testAnswerService.getAnswerById(answerId);
            Long qId = answer.getQuestion().getId();
            if(qIds.contains(answer.getQuestion().getId())){
                throw new InvalidScore();
            }
            else{
                qIds.add(qId);
                if (answer.getValid()) {
                    score++;
                }
            }
        }
        return score;
    }

    private User getUser(String token) throws UserNotFound {
        token = token.split(" ")[1];
        String email = jwtUtils.getEmailFromJwtToken(token);
        return userService.getUserByEmail(email);
    }

    public String getCIDForUserLevel(Long tagId, Integer userScore, Integer maxScore, User user){
//        Ar merge aici facut ceva cu distributie gaussiana, expert = +2sigma, intermediate = + sigma, ceva de genul
        Long levelId = 0L;
        if(userScore >= 0 && userScore < maxScore * 0.25){
            saveUserTest(user, tagId, null);
            return "";
        }
        else if(userScore >= maxScore * 0.25 && userScore < maxScore * 0.5){
            levelId = 1L;
        }
        else if(userScore >= maxScore * 0.5 && userScore < maxScore * 0.75){
            levelId = 2L;
        }
        else if(userScore >= maxScore * 0.75 && userScore <= maxScore){
            levelId = 3L;
        }
        TagLevel tagLevel = tagLevelService.getTagLevelByLevelIdAndTagId(tagId, levelId);
        saveUserTest(user, tagId, tagLevel);
        return tagLevel.getCID();
    }

    public void saveUserTest(User user, Long tagId, TagLevel tagLevel){
        Tag tag = tagService.getTagById(tagId);
        UserTest userTest = new UserTest(0L, user, tag, tagLevel, LocalDateTime.now(), false);
        userTestService.saveUserTest(userTest);
    }
}

