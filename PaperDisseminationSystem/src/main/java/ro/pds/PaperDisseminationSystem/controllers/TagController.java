package ro.pds.PaperDisseminationSystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.pds.PaperDisseminationSystem.entities.*;
import ro.pds.PaperDisseminationSystem.exceptions.CollectionOfTagsNotFound;
import ro.pds.PaperDisseminationSystem.exceptions.TagNotFound;
import ro.pds.PaperDisseminationSystem.exceptions.UserNotFound;
import ro.pds.PaperDisseminationSystem.security.jwt.JwtUtils;
import ro.pds.PaperDisseminationSystem.services.*;
import ro.pds.PaperDisseminationSystem.view.response.QuestionWithAnswersDto;
import ro.pds.PaperDisseminationSystem.view.response.TagDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ro.pds.PaperDisseminationSystem.util.Conversion.*;

@RestController
@RequestMapping("api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @Autowired
    private TestQuestionService testQuestionService;

    @Autowired
    private TestAnswerService testAnswerService;

    @Autowired
    private UserTestService userTestService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("")
    public ResponseEntity<?> getAllTags() {
        try {
            List<Tag> tags= tagService.listAllTags();
            List<TagDto> tagDtos = getTagDtosFromTags(tags);
            return new ResponseEntity<>(tagDtos, HttpStatus.OK);
        } catch (CollectionOfTagsNotFound e) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllQuestionsAndAnswers(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        User user;
        try {
            user = getUser(token);
        } catch (UserNotFound e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            tagService.getTagById(id);
        } catch (TagNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        UserTest latestUserTest = userTestService.getLatestUserTest(user.getId(), id);
        if (latestUserTest == null || latestUserTest.getCreatedAt().isBefore(LocalDateTime.now().minusMonths(3))) {
            List<QuestionWithAnswersDto> questionWithAnswersDto = new ArrayList<>();
            List<TestQuestion> testQuestions = testQuestionService.listAllQuestionsByTagId(id);
            for (TestQuestion testQuestion : testQuestions) {
                List<TestAnswer> answersForQuestion = testAnswerService.listAllAnswersByQuestionId(testQuestion.getId());
                questionWithAnswersDto.add(getQuestionWithAnswersDtoFromTag(testQuestion, answersForQuestion));
            }
            return new ResponseEntity<>(questionWithAnswersDto, HttpStatus.OK);
        } else {
            if (latestUserTest.getReceivedBadge() || latestUserTest.getTagLevel() == null) {
                return new ResponseEntity<>("Sorry, you have attempted the test too recently. Please wait at least 3 months before trying again.", HttpStatus.FORBIDDEN);
            } else{
                return new ResponseEntity<>("You have already attempted this test. Please obtain your badge.", HttpStatus.FORBIDDEN);
            }
        }
    }

    private User getUser(String token) throws UserNotFound {
        token = token.split(" ")[1];
        String email = jwtUtils.getEmailFromJwtToken(token);
        return userService.getUserByEmail(email);
    }
}