package ro.pds.PaperDisseminationSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.pds.PaperDisseminationSystem.entities.ArticleTagLevel;

import java.util.List;

@Repository
public interface ArticleTagLevelRepository extends JpaRepository<ArticleTagLevel, Long> {
    List<ArticleTagLevel> findByArticleId(Long articleId);
}
