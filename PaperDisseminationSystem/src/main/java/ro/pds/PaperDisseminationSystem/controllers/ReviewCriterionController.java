package ro.pds.PaperDisseminationSystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.pds.PaperDisseminationSystem.entities.*;
import ro.pds.PaperDisseminationSystem.exceptions.ArticleNotFound;
import ro.pds.PaperDisseminationSystem.exceptions.CollectionOfUserTestsNotFound;
import ro.pds.PaperDisseminationSystem.exceptions.UserNotFound;
import ro.pds.PaperDisseminationSystem.security.jwt.JwtUtils;
import ro.pds.PaperDisseminationSystem.services.*;
import ro.pds.PaperDisseminationSystem.view.response.ReviewCriterionDto;

import java.time.LocalDateTime;
import java.util.*;

import static ro.pds.PaperDisseminationSystem.util.Conversion.getReviewCriterionDtosFromReviewCriteria;

@RestController
@RequestMapping("api/reviewCriteria")
public class ReviewCriterionController {
    @Autowired
    private ReviewCriterionService reviewCriterionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ArticleTagLevelService articleTagLevelService;

    @Autowired
    private UserTestService userTestService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("")
    public ResponseEntity<?> getReviewCriteria(@RequestParam Optional<Long> articleId,
//                                               @RequestParam(defaultValue = "true") Boolean checkPermissions,
                                               @RequestHeader(name = "Authorization") String token) {
        if(articleId.isPresent()) {
            try {
                User user = getUser(token);
                Article article = articleService.getArticle(articleId.get());
                if(!isReviewPeriodValid(article)){
                    return new ResponseEntity<>("Sorry, the review period for this article is over!", HttpStatus.FORBIDDEN);
                }
                if (isReviewerTheAuthor(user, article)) {
                    return new ResponseEntity<>("Sorry, you are not allowed to review your own article!", HttpStatus.FORBIDDEN);
                }

                Review existingReview = reviewService.getReviewByUserIdAndArticleId(user.getId(), article.getId());
                if (existingReview == null) {
                    HashMap<String, String> tagsAndLevelsNeeded = getTagsAndLevelsNeededByArticleId(articleId.get());
                    List<UserTest> userTests = getLatestUserTestsByUserId(user.getId());
                    HashMap<String, String> tagsAndLevelsObtainedByUser = getTagsAndLevelsObtainedByUser(userTests);
                    boolean isUserReviewPossible = isUserReviewPossible(tagsAndLevelsNeeded, tagsAndLevelsObtainedByUser);

                    // If the author is the one who sent the req and the review period is over
//                    if (isReviewerTheAuthor(user, article)) {
//                        isUserReviewPossible = true;
//                    }

                    if (isUserReviewPossible) {
                        List<ReviewCriterion> reviewCriteria = reviewCriterionService.listAllReviewCriteria();
                        List<ReviewCriterionDto> reviewCriteriaDto = getReviewCriterionDtosFromReviewCriteria(reviewCriteria);
                        return new ResponseEntity<>(reviewCriteriaDto, HttpStatus.OK);
                    } else {
                        String userResponse = getResponseForUser(tagsAndLevelsNeeded, tagsAndLevelsObtainedByUser);
                        return new ResponseEntity<>(userResponse, HttpStatus.FORBIDDEN);
                    }
                } else {
                    return new ResponseEntity<>("Sorry, you have already reviewed this article!", HttpStatus.FORBIDDEN);
                }
            } catch (UserNotFound e) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } catch (ArticleNotFound e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            } catch (CollectionOfUserTestsNotFound e) {
                return new ResponseEntity<>("Sorry, no test results were found for this user. Please complete the required tests before reviewing the article.", HttpStatus.FORBIDDEN);
            }
        } else {
            // if the user does not want to offer a review, but the reviewCriteria are needed elsewhere
            List<ReviewCriterion> reviewCriteria = reviewCriterionService.listAllReviewCriteria();
            List<ReviewCriterionDto> reviewCriteriaDto = getReviewCriterionDtosFromReviewCriteria(reviewCriteria);
            return new ResponseEntity<>(reviewCriteriaDto, HttpStatus.OK);
        }
    }

    public Boolean isReviewPeriodValid(Article article){
        if(article.getReviewUntil().isAfter(LocalDateTime.now())){
            return true;
        }
        return false;
    }
    // Takes an article ID and returns a map of the required tag and level for the article
    public HashMap<String, String> getTagsAndLevelsNeededByArticleId(Long articleId) {
        List<ArticleTagLevel> articleTagLevelList = articleTagLevelService.listAllArticleTagLevelsByArticleId(articleId);
        HashMap<String, String> tagsAndLevelsNeeded = new HashMap<>();
        for (ArticleTagLevel articleTagLevel : articleTagLevelList) {
            tagsAndLevelsNeeded.put(articleTagLevel.getTagLevel().getTag().getName(), articleTagLevel.getTagLevel().getLevel().getName());
        }
        return tagsAndLevelsNeeded;
    }

    // Takes a user ID and returns a list of the latest test results for each tag
    public List<UserTest> getLatestUserTestsByUserId(Long userId) {
        List<UserTest> userTests = userTestService.getAllUserTestsByUserId(userId);
        Map<Long, UserTest> latestUserTests = new HashMap<>();
        for (UserTest userTest : userTests) {
            if(userTest.getTagLevel() != null) {
                Long tagId = userTest.getTagLevel().getTag().getId();
                if (!latestUserTests.containsKey(tagId) || userTest.getCreatedAt().isAfter(latestUserTests.get(tagId).getCreatedAt())) {
                    latestUserTests.put(tagId, userTest);
                }
            }
        }
        return new ArrayList<>(latestUserTests.values());
    }

    // Takes a list of user tests and returns a map of the user's test results for each tag
    public HashMap<String, String> getTagsAndLevelsObtainedByUser(List<UserTest> userTests) {
        HashMap<String, String> tagsAndLevelsObtainedByUser = new HashMap<>();
        for (UserTest userTest : userTests) {
            if(userTest.getTagLevel() != null && userTest.getReceivedBadge()){
                tagsAndLevelsObtainedByUser.put(userTest.getTagLevel().getTag().getName(), userTest.getTagLevel().getLevel().getName());
            }
        }
        return tagsAndLevelsObtainedByUser;
    }

    private String getResponseForUser(HashMap<String, String> tagsAndLevelsNeeded, HashMap<String, String> tagsAndLevelsObtainedByUser){
        String response = "Sorry, you cannot review this article! You need to have at least the ";
        StringBuilder levelsNeeded = new StringBuilder();
        for (Map.Entry<String, String> tagAndLevelNeeded : tagsAndLevelsNeeded.entrySet()) {
            levelsNeeded.append(tagAndLevelNeeded.getValue()).append(" level in the ").append(tagAndLevelNeeded.getKey()).append(" field and the ");
        }
        response += levelsNeeded.toString().replaceAll(" and the $", "");
        response += ".";
        return response;
    }
    private Boolean isUserReviewPossible(HashMap<String, String> tagsAndLevelsNeeded, HashMap<String, String> tagsAndLevelsObtainedByUser) {
        for (Map.Entry<String, String> tagAndLevelNeeded : tagsAndLevelsNeeded.entrySet()) {
            boolean tagFound = false;
            for (Map.Entry<String, String> obtainedTagAndLevel : tagsAndLevelsObtainedByUser.entrySet()) {
                if (obtainedTagAndLevel.getKey().equals(tagAndLevelNeeded.getKey()) &&
                        compareLevels(obtainedTagAndLevel.getValue(), tagAndLevelNeeded.getValue()) >= 0) {
                    tagFound = true;
                    break;
                }
            }
            if (!tagFound) {
                return false;
            }
        }
        return true;
    }

    private Integer compareLevels(String userLevel, String neededLevel) {
        if (userLevel.equals(neededLevel)) {
            return 0;
        } else if (userLevel.equals("Beginner")) {
            return -1;
        } else if (userLevel.equals("Intermediate")) {
            return neededLevel.equals("Beginner") ? 1 : -1;
        } else { // userLevel.equals("Expert")
            return neededLevel.equals("Intermediate") ? 1 : 2;
        }
    }

    private User getUser(String token) throws UserNotFound {
        token = token.split(" ")[1];
        String email = jwtUtils.getEmailFromJwtToken(token);
        return userService.getUserByEmail(email);
    }

    private Boolean isReviewerTheAuthor(User user, Article article){
        List<User> authors = article.getArticleAuthors();
        for (User author : authors) {
            if (Objects.equals(user.getEmail(), author.getEmail())) {
                return true;
            }
        }
        return false;
    }
}
