package ro.pds.PaperDisseminationSystem.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import javax.validation.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "accounts")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="metamask_id")
    private String metamaskId;

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String password;

    private String job;

    private String company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "article_authors",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id"))
    private List<Article> writtenArticles;


    public User(String name, String metamask_id, String email, String password, String job, String company) {
        this.name = name;
        this.metamaskId = metamask_id;
        this.email = email;
        this.password = password;
        this.job = job;
        this.company = company;
    }
}