package br.ufrgs.inf.pet.dinoapi.entity.notes;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static br.ufrgs.inf.pet.dinoapi.constants.NoteConstants.ANSWER_MAX;
import static br.ufrgs.inf.pet.dinoapi.constants.NoteConstants.QUESTION_MAX;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "notes", uniqueConstraints={
        @UniqueConstraint(columnNames={"question", "user_id"})
})
public class Note {
    private static final String SEQUENCE_NAME = "note_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "n_order", nullable = false)
    private Integer order;

    @Column(name = "question", length = QUESTION_MAX, nullable = false)
    private String question;

    @Column(name = "answer", length = ANSWER_MAX)
    private String answer;

    @Column(name = "answered", nullable = false)
    private Boolean answered;

    @Column(name = "last_update_day", nullable = false)
    private Date lastUpdate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(name = "note__note_tag",
            joinColumns = @JoinColumn(name = "note_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "note_tag_id", nullable = false))
    private List<NoteTag> tags;

    public Note() {
        this.tags = new ArrayList<>();
    }

    public Note(Date lastUpdate, Integer order, String question, List<NoteTag> tags, User user) {
        this.lastUpdate = lastUpdate;
        this.order = order;
        this.question = question;
        this.user = user;
        this.setAnswered(false);

        if (tags != null) {
            this.tags = tags;
        } else {
            this.tags = new ArrayList<>();
        }
    }

    public Long getId() {
        return id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        if (answer != null) {
            answer = answer.trim();
        }

        this.answer = answer;
    }

    public Boolean getAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<NoteTag> getTags() {
        return tags;
    }

    public void setTags(List<NoteTag> tags) {
        this.tags = tags;
    }

    public void addTags(List<NoteTag> tags) { this.tags.addAll(tags); }

    public void deleteTags(List<NoteTag> tags) {
        this.tags.removeAll(tags);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
