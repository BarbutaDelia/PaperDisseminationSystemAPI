package ro.pds.PaperDisseminationSystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.pds.PaperDisseminationSystem.entities.*;
import ro.pds.PaperDisseminationSystem.exceptions.ArticleNotFound;
import ro.pds.PaperDisseminationSystem.exceptions.CollectionOfReviewsNotFound;
import ro.pds.PaperDisseminationSystem.exceptions.UserNotFound;
import ro.pds.PaperDisseminationSystem.security.jwt.JwtUtils;
import ro.pds.PaperDisseminationSystem.services.*;
import ro.pds.PaperDisseminationSystem.view.request.AddReviewDto;
import ro.pds.PaperDisseminationSystem.view.response.ReviewDto;
import ro.pds.PaperDisseminationSystem.view.response.UserReviewDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

import static ro.pds.PaperDisseminationSystem.util.Conversion.getReviewDtoFromReview;
import static ro.pds.PaperDisseminationSystem.util.Conversion.getUserReviewDtoFromReview;

@RestController
@RequestMapping("api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewCriterionGradeService reviewCriterionGradeService;

    @Autowired
    private ReviewCriterionService reviewCriterionService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CheckUserReviewService checkUserReviewService;

    @Value("${articleUpload.price}") Double articleUploadPrice;

    @GetMapping("")
    public ResponseEntity<?> getReviewsForArticle(@RequestParam Optional<Long> articleId, @RequestParam Optional<Long> userId) {
        if (articleId.isPresent() && userId.isEmpty()) {
            try {
                articleService.getArticle(articleId.get());
                List<Review> articleReviews = reviewService.getReviewsByArticleId(articleId.get());
                List<ReviewDto> reviewDtos = new ArrayList<>();
                for (Review articleReview : articleReviews) {
                    List<ReviewCriterionGrade> articleGrades = reviewCriterionGradeService.listReviewGradesForReview(articleReview.getId());
                    ReviewDto reviewDto = getReviewDtoFromReview(articleGrades, articleReview);
                    reviewDtos.add(reviewDto);
                }
                return new ResponseEntity<>(reviewDtos, HttpStatus.OK);
            } catch (ArticleNotFound e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            } catch (CollectionOfReviewsNotFound e) {
                return new ResponseEntity<>(List.of(), HttpStatus.OK);
            }
        }
        if (articleId.isEmpty() && userId.isPresent()) {
            try {
                userService.getUserById(userId.get());
                List<Review> userReviews = reviewService.getReviewsByUserId(userId.get());
                List<UserReviewDto> reviewDtos = new ArrayList<>();
                for (Review userReview : userReviews) {
                    List<ReviewCriterionGrade> articleGrades = reviewCriterionGradeService.listReviewGradesForReview(userReview.getId());
                    UserReviewDto reviewDto = getUserReviewDtoFromReview(articleGrades, userReview);
                    reviewDtos.add(reviewDto);
                }
                return new ResponseEntity<>(reviewDtos, HttpStatus.OK);
            } catch (UserNotFound e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            } catch (CollectionOfReviewsNotFound e) {
                return new ResponseEntity<>(List.of(), HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity<>("Invalid or missing parameters. Please provide either an articleId, or a userId.", HttpStatus.BAD_REQUEST);
        }
    }

    //TODO: Verifica badgeuri
    @PostMapping(value = "")
    public ResponseEntity<?> addReview(@Valid @RequestBody AddReviewDto addReviewDto,
                                              @RequestHeader(name = "Authorization") String token) {
        if(addReviewDto.getRecommendation().length() == 0){
            return new ResponseEntity<>("Sorry, the review recommendation must not be empty!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            User user = getUser(token);
            Article article = articleService.getArticle(addReviewDto.getArticleId());
            if(!isReviewPeriodValid(article)){
                return new ResponseEntity<>("Sorry, the review period for this article is over!", HttpStatus.FORBIDDEN);
            }
            if(isReviewerTheAuthor(user, article)){
                return new ResponseEntity<>("Sorry, you are not allowed to review your own article!", HttpStatus.FORBIDDEN);
            }
            Review existingReview = reviewService.getReviewByUserIdAndArticleId(user.getId(), article.getId());
            HashMap<String, String> tagsAndLevelsNeeded = checkUserReviewService.getTagsAndLevelsNeededByArticleId(article.getId());
            List<UserTest> userTests = checkUserReviewService.getLatestUserTestsByUserId(user.getId());
            HashMap<String, String> tagsAndLevelsObtainedByUser = checkUserReviewService.getTagsAndLevelsObtainedByUser(userTests);
            boolean isUserReviewPossible = checkUserReviewService.isUserReviewPossible(tagsAndLevelsNeeded, tagsAndLevelsObtainedByUser);

            if (existingReview == null && isUserReviewPossible) {
                if (addReviewDto.getReviewCriteriaGrades().size() == 0) {
                    return new ResponseEntity<>("Please provide the review criteria grades!", HttpStatus.UNPROCESSABLE_ENTITY);
                }
                Double articleGrade = getArticleGrade(addReviewDto.getReviewCriteriaGrades());
                Review review = new Review(0L, article, user, addReviewDto.getRecommendation(), articleGrade, null, LocalDateTime.now());
                review = reviewService.saveReview(review);

                for (Map.Entry<Long, Integer> reviewCriterionGradeMap : addReviewDto.getReviewCriteriaGrades().entrySet()) {
                    ReviewCriterion reviewCriterion = reviewCriterionService.getReviewCriterion(reviewCriterionGradeMap.getKey());
                    ReviewCriterionGrade reviewCriterionGrade = new ReviewCriterionGrade(0L, review, reviewCriterion, reviewCriterionGradeMap.getValue());
                    reviewCriterionGradeService.saveReviewCriterionGrade(reviewCriterionGrade);
                }
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Sorry, you have already reviewed this article!", HttpStatus.FORBIDDEN);
            }
        }
        catch(UserNotFound e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        catch(ArticleNotFound e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public Boolean isReviewPeriodValid(Article article){
        if(article.getReviewUntil().isAfter(LocalDateTime.now())){
            return true;
        }
        return false;
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

    private Double getArticleGrade(HashMap<Long, Integer> reviewCriteriaGrades){
        Double articleGrade = 0D;
        for(Map.Entry<Long, Integer> reviewCriterionGrade : reviewCriteriaGrades.entrySet()){
            articleGrade += reviewCriterionGrade.getValue();
        }
        articleGrade /= reviewCriteriaGrades.size();
        return articleGrade;
    }

    private User getUser(String token) throws UserNotFound {
        token = token.split(" ")[1];
        String email = jwtUtils.getEmailFromJwtToken(token);
        return userService.getUserByEmail(email);
    }
}
