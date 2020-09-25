package br.ufrgs.inf.pet.dinoapi.entity.note;

import br.ufrgs.inf.pet.dinoapi.entity.User;

import javax.persistence.*;
import java.util.Date;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "note_version")
public class NoteVersion {

    private static final String SEQUENCE_NAME = "note_version_seq";

    public static final Long DEFAULT_VERSION = 0l;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "note_version", nullable = false)
    private Long noteVersion;

    @Column(name = "column_version", nullable = false)
    private Long columnVersion;

    @Column(name = "last_note_update", nullable = false)
    private Date lastNoteUpdate;

    @Column(name = "last_column_update", nullable = false)
    private Date lastColumnUpdate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public NoteVersion() { }

    public Long getId() {
        return id;
    }

    public Long getNoteVersion() {
        return noteVersion;
    }

    public void updateNoteVersion() {
        this.noteVersion = noteVersion + 1l;
    }

    public void setNoteVersion(Long version) {
        this.noteVersion = version;
    }

    public Long getColumnVersion() {
        return columnVersion;
    }

    public void setColumnVersion(Long columnVersion) {
        this.columnVersion = columnVersion;
    }

    public void updateColumnVersion() {
        this.columnVersion = columnVersion + 1l;
    }

    public Date getLastNoteUpdate() {
        return lastNoteUpdate;
    }

    public void setLastNoteUpdate(Date lastUpdate) {
        this.lastNoteUpdate = lastUpdate;
    }

    public Date getLastColumnUpdate() {
        return lastColumnUpdate;
    }

    public void setLastColumnUpdate(Date lastColumnUpdate) {
        this.lastColumnUpdate = lastColumnUpdate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
