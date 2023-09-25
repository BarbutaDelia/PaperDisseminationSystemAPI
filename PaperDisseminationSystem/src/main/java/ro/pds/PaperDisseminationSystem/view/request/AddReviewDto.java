package ro.pds.PaperDisseminationSystem.view.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewDto {
    private Long articleId;
    private HashMap<Long, Integer> reviewCriteriaGrades;
    private String recommendation;
}
