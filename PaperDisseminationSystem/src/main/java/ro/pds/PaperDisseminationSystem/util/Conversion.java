package ro.pds.PaperDisseminationSystem.util;

import org.springframework.data.util.Pair;
import ro.pds.PaperDisseminationSystem.entities.*;
import ro.pds.PaperDisseminationSystem.view.request.AddArticleDto;
import ro.pds.PaperDisseminationSystem.view.response.*;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Conversion {
    public static List<ArticleDto> getArticleDtosFromArticles(List<Article> articles){
        List<ArticleDto> articleDtos = new ArrayList<>();
        for(Article article: articles){
            List<String> tagNames = new ArrayList<>();
            for(TagLevel tagLevel: article.getTagLevels()){
                tagNames.add(tagLevel.getTag().getName());
            }
            ArticleDto articleDto = new ArticleDto(article.getId(), article.getTitle(), article.getDescription(), article.getCreatedAt(), tagNames);
            articleDtos.add(articleDto);
        }
        return articleDtos;
    }

    public static DetailedArticleDto getDetailedArticleDtoFromArticle(Article article){
        HashMap<String, String> tagLevels = new HashMap<>();
        List<String> authorNames = new ArrayList<>();
        for(TagLevel tagLevel: article.getTagLevels()){
            tagLevels.put(tagLevel.getTag().getName(), tagLevel.getLevel().getName());
        }
        for(User author: article.getArticleAuthors()){
            authorNames.add(author.getName());
        }
        return new DetailedArticleDto(article.getId(), article.getTitle(), article.getDescription(), article.getCreatedAt(), article.getReviewUntil(), tagLevels, authorNames, article.getGrade());
    }

    public static Article getArticleFromAddArticleDto(AddArticleDto addArticleDto, User uploader, List<User> authors, List<TagLevel> tagLevels, Path path){
        Article article =  new Article(0L, uploader, addArticleDto.getTitle(), addArticleDto.getDescription(), path.toString(), null, LocalDateTime.now(), LocalDateTime.now().plusDays(30), false, new ArrayList<>(), new ArrayList<>());
        article.addArticleAuthor(uploader);
        for(User author: authors){
            article.addArticleAuthor(author);
        }
        for(TagLevel tagLevel: tagLevels){
            article.addTagLevel(tagLevel);
        }

        return article;
    }

    public static TagLevelDto getTagLevelDtosFromTagLevels(List<Pair<String, String>> tagLevels){
        Comparator<Pair<String, String>> comparator = Comparator.comparing(Pair<String, String>::getFirst)
                .thenComparing((Pair<String, String> p) -> {
                    String second = p.getSecond();
                    if (second.equals("Beginner")) {
                        return 1;
                    } else if (second.equals("Intermediate")) {
                        return 2;
                    } else {
                        return 3;
                    }
                });

        tagLevels.sort(comparator);
        return new TagLevelDto(tagLevels);
    }

    public static List<TagDto> getTagDtosFromTags(List<Tag> tags){
        List<TagDto> tagDtos = new ArrayList<>();
        for(Tag tag: tags){
            TagDto tagDto = new TagDto(tag.getId(), tag.getName());
            tagDtos.add(tagDto);
        }
        return tagDtos;
    }

    public static QuestionWithAnswersDto getQuestionWithAnswersDtoFromTag(TestQuestion testQuestion, List<TestAnswer> answersForQuestion){
        List<Long> answerIds = new ArrayList<>();
        List<String> answerTexts = new ArrayList<>();
        for(TestAnswer answer: answersForQuestion){
            answerIds.add(answer.getId());
            answerTexts.add(answer.getAnswer());
        }
        return new QuestionWithAnswersDto(testQuestion.getId(), testQuestion.getQuestion(), answerIds, answerTexts);
    }

    public static List<ReviewCriterionDto> getReviewCriterionDtosFromReviewCriteria(List<ReviewCriterion> reviewCriteria){
        List<ReviewCriterionDto> reviewCriterionDtos = new ArrayList<>();
        for(ReviewCriterion reviewCriterion: reviewCriteria){
            ReviewCriterionDto reviewCriterionDto = new ReviewCriterionDto(reviewCriterion.getId(), reviewCriterion.getName(), reviewCriterion.getMinGrade(), reviewCriterion.getMaxGrade(), reviewCriterion.getDescription());
            reviewCriterionDtos.add(reviewCriterionDto);
        }
        return reviewCriterionDtos;
    }

    public static List<ArticleUploadedByUserDto> getArticleUploadedByUserDtosFromArticles(List<Article> articles){
        List<ArticleUploadedByUserDto> articleDtos = new ArrayList<>();
        for(Article article: articles){
            List<String> tagNames = new ArrayList<>();
            for(TagLevel tagLevel: article.getTagLevels()){
                tagNames.add(tagLevel.getTag().getName());
            }
            ArticleUploadedByUserDto articleDto = new ArticleUploadedByUserDto(article.getId(), article.getTitle(), article.getDescription(), article.getCreatedAt(), tagNames, article.getGrade());
            articleDtos.add(articleDto);
        }
        return articleDtos;
    }

    public static ReviewDto getReviewDtoFromReview(List<ReviewCriterionGrade> articleGrades, Review articleReview){
        HashMap<Long, Integer> criteriaGrades = new HashMap<>();
        Integer sumOfGrades = 0;
        for(ReviewCriterionGrade grade: articleGrades) {
            criteriaGrades.put(grade.getReviewCriterion().getId(), grade.getValue());
            sumOfGrades += grade.getValue();
        }
        return new ReviewDto(articleReview.getId(), criteriaGrades, articleReview.getRecommendation(), (double) (sumOfGrades / criteriaGrades.size()));
    }

    public static UserReviewDto getUserReviewDtoFromReview(List<ReviewCriterionGrade> articleGrades, Review userReview){
        HashMap<Long, Integer> criteriaGrades = new HashMap<>();
        Integer sumOfGrades = 0;
        for(ReviewCriterionGrade grade: articleGrades) {
            criteriaGrades.put(grade.getReviewCriterion().getId(), grade.getValue());
            sumOfGrades += grade.getValue();
        }
        return new UserReviewDto(userReview.getId(), userReview.getArticle().getTitle(), criteriaGrades, userReview.getRecommendation(), (double) (sumOfGrades / criteriaGrades.size()), userReview.getEarnedAmount());
    }
}
