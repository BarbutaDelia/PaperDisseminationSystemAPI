package ro.pds.PaperDisseminationSystem.exceptions;

public class LevelNotFound extends RuntimeException {

    public LevelNotFound(String name) {
        super("Could not find level " + name);
    }
    public LevelNotFound(Long id) {
        super("Could not find level " + id);
    }
}
