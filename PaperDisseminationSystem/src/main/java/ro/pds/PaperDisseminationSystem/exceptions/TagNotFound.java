package ro.pds.PaperDisseminationSystem.exceptions;

public class TagNotFound extends RuntimeException {

    public TagNotFound(String name) {
        super("Could not find tag " + name);
    }
    public TagNotFound(Long id) {
        super("Could not find tag " + id);
    }
}
