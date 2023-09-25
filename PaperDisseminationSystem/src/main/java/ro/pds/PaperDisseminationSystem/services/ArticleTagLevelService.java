package ro.pds.PaperDisseminationSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.pds.PaperDisseminationSystem.entities.ArticleTagLevel;
import ro.pds.PaperDisseminationSystem.repositories.ArticleTagLevelRepository;

import java.util.List;

@Service
@Transactional
public class ArticleTagLevelService {
    @Autowired
    private ArticleTagLevelRepository articleTagLevelRepository;

    public List<ArticleTagLevel> listAllArticleTagLevelsByArticleId(Long articleId) {
        return articleTagLevelRepository.findByArticleId(articleId);
    }
}
