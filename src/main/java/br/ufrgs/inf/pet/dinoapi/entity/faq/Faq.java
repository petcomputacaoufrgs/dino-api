package br.ufrgs.inf.pet.dinoapi.entity.faq;

import br.ufrgs.inf.pet.dinoapi.model.faq.FaqSaveRequestModel;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "faq")
public class Faq implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "faq_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(mappedBy = "faq")
    private FaqVersion version;

    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Valid
    @OneToMany(mappedBy = "faq", cascade = CascadeType.ALL)
    private List<FaqItem> items;


    public Faq() {
        this.items = new ArrayList<>();
    }

    public Faq(FaqSaveRequestModel faqSaveRequestModel) {
        this.setTitle(faqSaveRequestModel.getTitle());
        this.setItems(new ArrayList<>());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FaqItem> getItems() {
        return items;
    }

    public void setItems(List<FaqItem> items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FaqVersion getVersion() {
        return version;
    }

    public void setVersion(FaqVersion version) {
        this.version = version;
    }
}
