package br.ufrgs.inf.pet.dinoapi.entity.faq;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "faq_version")
public class FaqVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "faq_version_seq";

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
    @JoinColumn(name = "faq_id", referencedColumnName = "id")
    private Faq faq;

    public FaqVersion() {}

    public FaqVersion(Faq faq) {
        this.setVersion(1L);
        this.setFaq(faq);
    }

    public FaqVersion(Long version, Faq faq) {
        this.setVersion(version);
        this.setFaq(faq);
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

    public Faq getFaq() {
        return faq;
    }

    public void setFaq(Faq faq) {
        this.faq = faq;
    }
}
