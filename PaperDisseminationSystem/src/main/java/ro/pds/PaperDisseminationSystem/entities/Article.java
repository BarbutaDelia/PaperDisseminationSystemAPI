package ro.pds.PaperDisseminationSystem.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "uploaded_by")
    private User user;

    private String title;

    private String description;

    private String path;

    private Double grade;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column(name = "review_until")
    private LocalDateTime reviewUntil;

    @Column(name = "payment_status")
    private Boolean paymentStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "article_tag_levels",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_level_id"))
    private List<TagLevel> tagLevels;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "article_authors",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<User> articleAuthors;

    public void addTagLevel(TagLevel tagLevel){
        tagLevels.add(tagLevel);
    }

    public void addArticleAuthor(User author){
        articleAuthors.add(author);
    }
}
