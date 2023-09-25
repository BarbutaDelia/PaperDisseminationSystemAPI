package ro.pds.PaperDisseminationSystem.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private User user;

    private String recommendation;

    @Column(name = "article_grade")
    private Double articleGrade;

    @Column(name = "earned_amount")
    private Double earnedAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
