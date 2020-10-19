package br.ufrgs.inf.pet.dinoapi.entity.note;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "note_tag")
public class NoteTag {

    private static final String SEQUENCE_NAME = "note_tag_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Note> notes;

    public NoteTag() {
        this.notes = new ArrayList<>();
    }

    public NoteTag(String name) {
        this.name = name;
        this.notes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
