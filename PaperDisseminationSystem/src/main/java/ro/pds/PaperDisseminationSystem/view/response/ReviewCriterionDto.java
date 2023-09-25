package ro.pds.PaperDisseminationSystem.view.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCriterionDto {
    private Long id;
    private String name;
    private Integer minGrade;
    private Integer maxGrade;
    private String description;
}
