package ro.pds.PaperDisseminationSystem.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.pds.PaperDisseminationSystem.entities.Article;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
        Article findArticleById(Long id);

        List<Article> findByUserIdAndPaymentStatus(Long userId, Boolean paymentStatus, Sort id);

        List<Article> findByPaymentStatusTrue(Pageable pageable);

        List<Article> findByPaymentStatusFalse(Sort id);

        @Query("SELECT a FROM Article a WHERE a.reviewUntil < :now AND a.paymentStatus = true AND a.grade IS NULL")
        List<Article> findAllWithReviewPeriodExpired(LocalDateTime now);

        @Query("SELECT a FROM Article a WHERE a.title LIKE %:searchQuery% OR a.description LIKE %:searchQuery%")
        List<Article> findByTitleOrDescription(String searchQuery);

}
