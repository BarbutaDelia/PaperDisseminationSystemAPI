package ro.pds.PaperDisseminationSystem.view.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailedArticleDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime created_at;
    private LocalDateTime review_until;
    private HashMap<String, String> tagLevels;
    private List<String> authorNames;
    private Double grade;
}
