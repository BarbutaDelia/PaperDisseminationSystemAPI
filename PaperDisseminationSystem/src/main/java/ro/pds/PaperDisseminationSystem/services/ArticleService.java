package ro.pds.PaperDisseminationSystem.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.pds.PaperDisseminationSystem.entities.Article;
import ro.pds.PaperDisseminationSystem.exceptions.ArticleNotFound;
import ro.pds.PaperDisseminationSystem.exceptions.CollectionOfArticlesNotFound;
import ro.pds.PaperDisseminationSystem.repositories.ArticleRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> listAllArticles() {
        if(!articleRepository.findAll().isEmpty())
            return articleRepository.findAll();
        else
            throw new CollectionOfArticlesNotFound();
    }

    public List<Article> listAllPaidArticles() {
        List<Article> paidArticles = articleRepository.findByPaymentStatusTrue(PageRequest.of(0, 20, Sort.by("id").descending()));
        if (paidArticles.isEmpty()) {
            throw new CollectionOfArticlesNotFound();
        }
        return paidArticles;
    }

    public List<Article> listAllUserArticles(Long userId, Boolean paymentStatus) {
        List<Article> userArticles = articleRepository.findByUserIdAndPaymentStatus(userId, paymentStatus, Sort.by("id").descending());
        if (userArticles.isEmpty()) {
            throw new CollectionOfArticlesNotFound();
        }
        return userArticles;
    }

    public List<Article> listAllUnpaidArticles() {
        List<Article> paidArticles = articleRepository.findByPaymentStatusFalse(Sort.by("id").descending());
        if (paidArticles.isEmpty()) {
            throw new CollectionOfArticlesNotFound();
        }
        return paidArticles;
    }

    public List<Article> listAllArticlesWithExpiredReviewPeriods(){
        List<Article> expiredReviewPeriodArticles = articleRepository.findAllWithReviewPeriodExpired(LocalDateTime.now());
        if(expiredReviewPeriodArticles.isEmpty()){
            throw new CollectionOfArticlesNotFound();
        }
        return expiredReviewPeriodArticles;
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article getArticle(Long id) {
        if(articleRepository.findById(id).isPresent())
            return articleRepository.findById(id).get();
        else
            throw new ArticleNotFound(id);
    }

    public void deleteArticle(Long id){
        if(articleRepository.findById(id).isPresent()) {
            articleRepository.deleteById(id);
        }
        else
            throw new ArticleNotFound(id);
    }

    public List<Article> searchArticles(String searchQuery){
        List<Article> articles = articleRepository.findByTitleOrDescription(searchQuery);
        if (articles.isEmpty()) {
            throw new CollectionOfArticlesNotFound();
        }
        return articles;
    }
}
