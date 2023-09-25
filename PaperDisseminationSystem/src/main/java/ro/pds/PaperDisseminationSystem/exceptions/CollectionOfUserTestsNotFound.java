package ro.pds.PaperDisseminationSystem.exceptions;

public class CollectionOfUserTestsNotFound extends RuntimeException {
    public CollectionOfUserTestsNotFound(Long id) {
        super("User " + id + " did not complete any tests.");
    }
}
