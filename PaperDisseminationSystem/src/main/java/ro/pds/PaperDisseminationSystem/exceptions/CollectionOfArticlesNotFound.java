package ro.pds.PaperDisseminationSystem.exceptions;

public class CollectionOfArticlesNotFound extends RuntimeException {
    public CollectionOfArticlesNotFound() {
        super("Could not find articles.");
    }
}