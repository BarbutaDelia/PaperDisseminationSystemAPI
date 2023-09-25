package ro.pds.PaperDisseminationSystem.exceptions;

public class TagLevelNotFound extends RuntimeException {
    public TagLevelNotFound(Long tagId, Long levelId) {
        super("Could not find the following entry: tag_id =  " + tagId + ", level_id = " + levelId);
    }
    public TagLevelNotFound(String CID) {
        super("Could not find the entry with the CID: " + CID);
    }
}
