package ro.pds.PaperDisseminationSystem.view.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionWithAnswersDto {
    private Long questionId;
    private String question;
    private List<Long> answerIds;
    private List<String> answers;
}
