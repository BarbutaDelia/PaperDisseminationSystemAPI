package ro.pds.PaperDisseminationSystem.view.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReviewDto {
    private Long id;
    private String articleName;
    private HashMap<Long, Integer> criteriaGrades;
    private String recommendation;
    private Double grade;
    private Double earnedAmount;
}
