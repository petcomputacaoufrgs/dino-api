package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "contact_version")
public class ContactVersion {

    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "contact_version_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Versão não pode ser nula.")
    @Column(name = "version")
    private Long version;

    @OneToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
