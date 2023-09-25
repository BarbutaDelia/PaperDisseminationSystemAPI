package ro.pds.PaperDisseminationSystem.view.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ArticleUploadedByUserDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime created_at;
    private List<String> tagNames;
    private Double articleGrade;
}