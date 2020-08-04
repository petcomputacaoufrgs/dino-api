package br.ufrgs.inf.pet.dinoapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "note", uniqueConstraints={
        @UniqueConstraint(columnNames={"question", "user_id"})
})
public class Note implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "note_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @Column(name = "n_order")
    private Integer order;

    @Basic(optional = false)
    @Column(name = "question", length = 500)
    private String question;

    @Column(name = "answer", length = 1000)
    private String answer;

    @Basic(optional = false)
    @Column(name = "answered")
    private Boolean answered;

    @Basic(optional = false)
    @Column(name = "last_update_day")
    private Date lastUpdate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "note__note_tag",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "note_tag_id"))
    private List<NoteTag> tags;

    public Note() {}

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
