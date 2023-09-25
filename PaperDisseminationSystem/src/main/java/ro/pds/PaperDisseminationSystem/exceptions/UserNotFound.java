package ro.pds.PaperDisseminationSystem.exceptions;

public class UserNotFound extends RuntimeException {

    public UserNotFound(String information) {
        super("Could not find user " + information);
    }
    public UserNotFound(Long id) {
        super("Could not find user with id " + id);
    }
}
