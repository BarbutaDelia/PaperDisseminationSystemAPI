package ro.pds.PaperDisseminationSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.pds.PaperDisseminationSystem.entities.ReviewCriterionGrade;
import ro.pds.PaperDisseminationSystem.exceptions.ReviewGradesNotFound;
import ro.pds.PaperDisseminationSystem.repositories.ReviewCriterionGradeRepository;

import java.util.List;

@Service
@Transactional
public class ReviewCriterionGradeService {
    @Autowired
    private ReviewCriterionGradeRepository reviewCriterionGradeRepository;

    public ReviewCriterionGrade saveReviewCriterionGrade(ReviewCriterionGrade reviewCriterionGrade) {
        return reviewCriterionGradeRepository.save(reviewCriterionGrade);
    }

    public List<ReviewCriterionGrade> listReviewGradesForReview(Long reviewId){
        List<ReviewCriterionGrade> reviewGrades = reviewCriterionGradeRepository.findByReviewId(reviewId);
        if(reviewGrades.isEmpty()){
            throw new ReviewGradesNotFound(reviewId);
        }
        return reviewGrades;
    }
}
