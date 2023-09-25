package ro.pds.PaperDisseminationSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.pds.PaperDisseminationSystem.entities.Article;
import ro.pds.PaperDisseminationSystem.entities.ArticleTagLevel;
import ro.pds.PaperDisseminationSystem.entities.User;
import ro.pds.PaperDisseminationSystem.entities.UserTest;
import ro.pds.PaperDisseminationSystem.exceptions.UserNotFound;
import ro.pds.PaperDisseminationSystem.security.jwt.JwtUtils;
import ro.pds.PaperDisseminationSystem.services.*;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class CheckUserReviewService {
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
    public Boolean isUserReviewPossible(HashMap<String, String> tagsAndLevelsNeeded, HashMap<String, String> tagsAndLevelsObtainedByUser) {
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
