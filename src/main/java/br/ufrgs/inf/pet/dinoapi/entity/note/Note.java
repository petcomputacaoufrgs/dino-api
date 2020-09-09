package br.ufrgs.inf.pet.dinoapi.entity.note;

import br.ufrgs.inf.pet.dinoapi.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "note")
public class Note {
    private static final String SEQUENCE_NAME = "note_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "n_order", nullable = false)
    private Integer order;

    @Column(name = "question", length = 250, nullable = false)
    private String question;

    @Column(name = "answer", length = 500)
    private String answer;

    @Column(name = "last_update_day", nullable = false)
    private Date lastUpdate;

    @ManyToOne
    @JoinColumn(name = "note_column_id", nullable = false)
    private NoteColumn noteColumn;

    @ManyToMany
    @JoinTable(name = "note__note_tag",
            joinColumns = @JoinColumn(name = "note_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "note_tag_id", nullable = false))
    private List<NoteTag> tags;

    public Note() {
        this.tags = new ArrayList<>();
    }

    public Note(NoteColumn noteColumn, Integer order) {
        this.setOrder(order);
        this.setNoteColumn(noteColumn);
        this.tags = new ArrayList<>();
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

    public NoteColumn getNoteColumn() {
        return noteColumn;
    }

    public void setNoteColumn(NoteColumn noteColumn) {
        this.noteColumn = noteColumn;
    }
}
