package ro.pds.PaperDisseminationSystem.exceptions;

public class CollectionOfReviewCriteriaNotFound extends RuntimeException {
    public CollectionOfReviewCriteriaNotFound() {
        super("Could not find review criteria.");
    }
}
