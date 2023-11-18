package br.grupo2.springdemo.domain;

import lombok.*;
import br.grupo2.springdemo.audit.Auditable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "question")
public class Question extends Auditable<Account> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "body", columnDefinition = "text", length = 65536, nullable = false)
    @NotBlank(message = "Question body can't be empty")
    private String body;

    @Column(name = "views")
    private Long views = 0L;

    @ManyToOne(cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "author_id")
    private Account author;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        name = "question_tag",
        joinColumns = @JoinColumn(name = "question_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    @ManyToMany(cascade = {
            CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "question_like",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Account> positiveVotes;

    @ManyToMany(cascade = {
            CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "question_dislike",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Account> negativeVotes;


    // Métodos para incrementar visualizações
    public void incrementViews() {
        this.views = (this.views != null) ? this.views + 1 : 1L;
    }

    public void addTag(Tag tag) {
        if (tags == null) {
            tags = new HashSet<>();
        }
        tags.add(tag);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public void addPositiveVote(Account author) {
        if (positiveVotes == null) {
            positiveVotes = new HashSet<>();
        }
        positiveVotes.add(author);
    }

    public void removePositiveVote(Account author) {
        positiveVotes.remove(author);
    }

    public void addNegativeVote(Account author) {
        if (negativeVotes == null) {
            negativeVotes = new HashSet<>();
        }
        negativeVotes.add(author);
    }

    public void removeNegativeVote(Account author) {
        negativeVotes.remove(author);
    }

    public Integer getRating() {
        if (positiveVotes != null && negativeVotes != null) {
            return positiveVotes.size() - negativeVotes.size();
        }
        return 0;
    }

    public boolean hasAcceptedAnswer() {
        return answers.stream().anyMatch(Answer::getIsAccepted);
    }

    public String getFormattedDate(LocalDateTime timestamp, String pattern) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return timestamp.format(formatter);
    }

}
