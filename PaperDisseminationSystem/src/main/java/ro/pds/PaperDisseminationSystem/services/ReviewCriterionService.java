package ro.pds.PaperDisseminationSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.pds.PaperDisseminationSystem.exceptions.CollectionOfReviewCriteriaNotFound;
import ro.pds.PaperDisseminationSystem.exceptions.ReviewCriterionNotFound;
import ro.pds.PaperDisseminationSystem.entities.ReviewCriterion;
import ro.pds.PaperDisseminationSystem.repositories.ReviewCriterionRepository;

import java.util.List;

@Service
@Transactional
public class ReviewCriterionService {
    @Autowired
    private ReviewCriterionRepository reviewCriterionRepository;

    public List<ReviewCriterion> listAllReviewCriteria() {
        if(!reviewCriterionRepository.findAll().isEmpty())
            return reviewCriterionRepository.findAll();
        else
            throw new CollectionOfReviewCriteriaNotFound();
    }

    public ReviewCriterion getReviewCriterion(Long id) {
        if(reviewCriterionRepository.findById(id).isPresent())
            return reviewCriterionRepository.findById(id).get();
        else
            throw new ReviewCriterionNotFound(id);
    }

}
