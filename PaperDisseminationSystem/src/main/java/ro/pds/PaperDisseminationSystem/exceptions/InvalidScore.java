package ro.pds.PaperDisseminationSystem.exceptions;

public class InvalidScore extends RuntimeException {
    public InvalidScore() {
        super("You have selected too many answers for one question!");
    }
}
