package ro.pds.PaperDisseminationSystem.exceptions;

public class ReviewCriterionNotFound extends RuntimeException{
    public ReviewCriterionNotFound(Long id) {
        super("Could not find review criterion " + id);
    }
}
