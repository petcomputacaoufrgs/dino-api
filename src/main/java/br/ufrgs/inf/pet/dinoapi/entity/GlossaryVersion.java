package br.ufrgs.inf.pet.dinoapi.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "glossary_version")
public class GlossaryVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "glossary_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @NotNull(message = "Versão não pode ser nula.")
    @Column(name = "version")
    private Long version;

    @Basic(optional = false)
    @NotNull(message = "Data da última atualização não pode ser nula.")
    @Column(name = "last_update")
    private Date lastUpdate;

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void updateVersion() {
        this.version = this.version + 1;
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
}
