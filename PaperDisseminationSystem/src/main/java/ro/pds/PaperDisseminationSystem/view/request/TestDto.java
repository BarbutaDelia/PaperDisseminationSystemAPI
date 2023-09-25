package ro.pds.PaperDisseminationSystem.view.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestDto {
    Long tagId;
    List<Long> answerIds;
}
