package ro.pds.PaperDisseminationSystem.exceptions;

public class ReviewGradesNotFound extends RuntimeException{
    public ReviewGradesNotFound(Long id) {
        super("Could not find review grades for review " + id);
    }
}