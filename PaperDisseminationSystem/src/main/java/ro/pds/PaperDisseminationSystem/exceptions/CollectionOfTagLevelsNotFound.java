package ro.pds.PaperDisseminationSystem.exceptions;

public class CollectionOfTagLevelsNotFound extends RuntimeException {
    public CollectionOfTagLevelsNotFound() {
        super("Could not find tags and levels.");
    }
}
