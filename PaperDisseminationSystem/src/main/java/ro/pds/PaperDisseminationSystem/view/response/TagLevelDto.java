package ro.pds.PaperDisseminationSystem.view.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagLevelDto {
    private List<Pair<String, String>> tagLevel;
}
