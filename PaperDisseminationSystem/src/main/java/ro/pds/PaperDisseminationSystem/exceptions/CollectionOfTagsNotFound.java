package ro.pds.PaperDisseminationSystem.exceptions;

public class CollectionOfTagsNotFound extends RuntimeException {
    public CollectionOfTagsNotFound() {
        super("Could not find tags.");
    }
}
