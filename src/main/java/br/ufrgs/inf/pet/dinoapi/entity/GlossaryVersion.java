package br.ufrgs.inf.pet.dinoapi.entity;

import javax.persistence.*;
import java.util.Date;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "glossary_version")
public class GlossaryVersion {
    private static final String SEQUENCE_NAME = "glossary_seq";

    public final Long DEFAULT_VERSION = 0l;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "last_update", nullable = false)
    private Date lastUpdate;

    public GlossaryVersion() {
        this.lastUpdate = new Date();
        this.version = DEFAULT_VERSION;
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void updateVersion() {
        this.version = version + 1l;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
