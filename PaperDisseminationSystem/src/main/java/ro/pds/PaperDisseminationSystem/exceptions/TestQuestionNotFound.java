package ro.pds.PaperDisseminationSystem.exceptions;

public class TestQuestionNotFound extends RuntimeException {
    public TestQuestionNotFound(Long id) {
        super("Could not find the question with the id " + id + ".");
    }
}
