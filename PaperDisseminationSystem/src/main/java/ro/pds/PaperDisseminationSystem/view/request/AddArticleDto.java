package ro.pds.PaperDisseminationSystem.view.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddArticleDto {
    private String title;
    private String description;
    private List<String> authors;
    private HashMap<String, String> tagLevels;
}
