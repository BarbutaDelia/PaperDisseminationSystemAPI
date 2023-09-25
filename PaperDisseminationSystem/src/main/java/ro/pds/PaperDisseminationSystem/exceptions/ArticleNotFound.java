package ro.pds.PaperDisseminationSystem.exceptions;

public class ArticleNotFound extends RuntimeException {
    public ArticleNotFound(Long id) {
        super("Could not find article " + id);
    }
}
