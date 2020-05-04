package br.ufrgs.inf.pet.dinoapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "note", uniqueConstraints={
        @UniqueConstraint(columnNames={"n_order", "user_id"}),
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
    @NotNull(message = "A pergunta não pode ser nula.")
    @Size(min = 1, max = 500, message = "A pergunta deve conter entre 1 e 500 caracteres.")
    @Column(name = "question", length = 500)
    private String question;

    @Size(min = 1, max = 1000, message = "A resposta deve conter entre 1 e 1000 caracteres.")
    @Column(name = "answer", length = 1000)
    private String answer;

    @Basic(optional = false)
    @NotNull(message = "Dado de resposta não pode ser nulo.")
    @Column(name = "answered")
    private Boolean answered;

    @Basic(optional = false)
    @NotNull(message = "A data da última atualização não pode ser nula.")
    @Column(name = "last_update_day")
    private LocalDateTime lastUpdate;

    @JsonIgnore
    @Valid
    @ManyToOne
    @NotNull(message = "O usuário não pode ser nulo.")
    @JoinColumn(name = "user_id")
    private User user;

    @Valid
    @ManyToMany
    @JoinTable(name = "note__note_tag",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "note_tag_id"))
    private List<NoteTag> tags;

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
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<NoteTag> getTags() {
        return tags;
    }

    public void setTags(List<NoteTag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
