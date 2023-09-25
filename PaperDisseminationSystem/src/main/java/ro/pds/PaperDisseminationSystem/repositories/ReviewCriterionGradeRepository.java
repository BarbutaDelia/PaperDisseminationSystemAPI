package ro.pds.PaperDisseminationSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.pds.PaperDisseminationSystem.entities.ReviewCriterionGrade;

import java.util.List;

@Repository
public interface ReviewCriterionGradeRepository extends JpaRepository<ReviewCriterionGrade, Long> {
    List<ReviewCriterionGrade> findByReviewId(Long reviewId);
}
