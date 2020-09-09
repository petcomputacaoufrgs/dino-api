package br.ufrgs.inf.pet.dinoapi.entity;

import javax.persistence.*;
import java.util.Date;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "note_version")
public class NoteVersion {

    private static final String SEQUENCE_NAME = "note_version_seq";

    public final Long DEFAULT_VERSION = 0L;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "last_update", nullable = false)
    private Date lastUpdate;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public NoteVersion() { }

    public NoteVersion(User user) {
        this.user = user;
        this.lastUpdate = new Date();
        this.version = this.DEFAULT_VERSION;
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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
