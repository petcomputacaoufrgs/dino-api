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

    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "version_column", nullable = false)
    private Long columnVersion;

    @Column(name = "last_update", nullable = false)
    private Date lastUpdate;

    @Column(name = "last_column_update", nullable = false)
    private Date lastColumnUpdate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public NoteVersion() { }

    public NoteVersion(User user) {
        this.user = user;
        this.lastUpdate = new Date();
        this.lastColumnUpdate = new Date();
        this.version = this.DEFAULT_VERSION;
        this.columnVersion = this.DEFAULT_VERSION;
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void updateVersion() {
        this.version = version + 1l;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getColumnVersion() {
        return columnVersion;
    }

    public void setColumnVersion(Long versionColumn) {
        this.columnVersion = columnVersion;
    }

    public void updateColumnVersion() {
        this.columnVersion = columnVersion + 1l;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
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
