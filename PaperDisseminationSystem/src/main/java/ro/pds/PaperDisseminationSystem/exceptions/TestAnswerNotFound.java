package ro.pds.PaperDisseminationSystem.exceptions;

public class TestAnswerNotFound extends RuntimeException {
    public TestAnswerNotFound(Long id) {
        super("Could not find the answer with the id " + id + ".");
    }
}
