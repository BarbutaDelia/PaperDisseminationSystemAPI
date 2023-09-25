package ro.pds.PaperDisseminationSystem.exceptions;

public class CollectionOfReviewsNotFound extends RuntimeException {
    public CollectionOfReviewsNotFound(Long articleId) {
        super("Could not find any reviews for the article " + articleId);
    }

}
