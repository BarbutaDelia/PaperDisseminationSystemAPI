package ro.pds.PaperDisseminationSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.pds.PaperDisseminationSystem.entities.Review;
import ro.pds.PaperDisseminationSystem.exceptions.CollectionOfReviewsNotFound;
import ro.pds.PaperDisseminationSystem.exceptions.ReviewNotFound;
import ro.pds.PaperDisseminationSystem.repositories.ReviewRepository;

import java.util.List;

@Service
@Transactional
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public Review getReview(Long id) {
        if(reviewRepository.findById(id).isPresent())
            return reviewRepository.findById(id).get();
        else
            throw new ReviewNotFound(id);
    }

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review getReviewByUserIdAndArticleId(Long userId, Long articleId){
        return reviewRepository.findByArticleIdAndUserId(articleId, userId);
    }

    public List<Review> getReviewsByArticleId(Long articleId){
        List<Review> reviews = reviewRepository.findReviewByArticleId(articleId, Sort.by("id").descending());
        if(reviews.isEmpty()){
            throw new CollectionOfReviewsNotFound(articleId);
        }
        return reviews;
    }

    public List<Review> getReviewsByUserId(Long userId){
        List<Review> reviews = reviewRepository.findReviewByUserId(userId, Sort.by("id").descending());
        if(reviews.isEmpty()){
            throw new CollectionOfReviewsNotFound(userId);
        }
        return reviews;
    }

}