package ro.pds.PaperDisseminationSystem.exceptions;

public class ReviewNotFound extends RuntimeException {
    public ReviewNotFound(Long id) {
        super("Could not find review " + id);
    }
}
