package br.ufrgs.inf.pet.dinoapi.entity;

import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryItemSaveModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryItemUpdateModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "glossary_item")
public class GlossaryItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "glossary_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @NotNull(message = "Título não pode ser nulo.")
    @Size(min = 1, max = 100, message = "O titulo deve conter entre 1 e 100 caracteres.")
    @Column(name = "title", length = 100, unique = true)
    private String title;

    @Size(min = 0, max = 20, message = "O texto deve conter entre 0 e 20 caracteres.")
    @Column(name = "subtitle", length = 20)
    private String subtitle;

    @Basic(optional = false)
    @NotNull(message = "Texto não pode ser nulo.")
    @Size(min = 0, max = 1000, message = "O texto deve conter entre 0 e 1000 caracteres.")
    @Column(name = "text", length = 1000)
    private String text;
    
    @Size(min = 0, max = 20000, message = "O texto completo deve conter entre 0 e 20000 caracteres.")
    @Column(name = "full_text", length = 20000)
    private String fullText;

    @Basic(optional = false)
    @NotNull(message = "Dado de existencia não pode ser nulo.")
    @Column(name = "exists")
    private Boolean exists;

    public GlossaryItem() {}

    public void setByGlossarySaveModel(GlossaryItemSaveModel glossaryItemSaveModel) {
        this.title = glossaryItemSaveModel.getTitle();
        this.subtitle = glossaryItemSaveModel.getSubtitle();
        this.text = glossaryItemSaveModel.getText();
        this.fullText = glossaryItemSaveModel.getFullText().trim();
        this.exists = true;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getText() {
        return text;
    }

    public String getFullText() {
        return fullText;
    }

    public Boolean getExists() {
        return exists;
    }

    /**
     * Atualiza o GlossaryItem baseado na {@link GlossaryItemUpdateModel} se houverem mudanças aplicaveis.
     * @param updateModel {@link GlossaryItemUpdateModel} com as atualizações dados
     * @return True se houver modificações e False se não houver modificações
     */
    public Boolean update(GlossaryItemUpdateModel updateModel) {
        Boolean updated = false;

        if (!this.title.equals(updateModel.getTitle())) {
            this.title = updateModel.getTitle();
            updated = true;
        }

        if (!this.subtitle.equals(updateModel.getSubtitle())) {
            this.subtitle = updateModel.getSubtitle();
            updated = true;
        }

        if (!this.text.equals(updateModel.getText())) {
            this.text = updateModel.getText();
            updated = true;
        }

        // TODO: discutir melhor
        if(this.fullText == null || !this.fullText.equals(updateModel.getFullText().trim())) {
            this.fullText = updateModel.getFullText();
            updated = true;
        }

        if (this.exists != updateModel.getExists()) {
            this.exists = updateModel.getExists();
            updated = true;
        }

        return updated;
    }
}


