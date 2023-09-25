package ro.pds.PaperDisseminationSystem.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.pds.PaperDisseminationSystem.entities.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByArticleIdAndUserId(Long articleId, Long userId);

    List<Review> findReviewByArticleId(Long articleId, Sort id);
    List<Review> findReviewByUserId(Long userId, Sort id);
}
