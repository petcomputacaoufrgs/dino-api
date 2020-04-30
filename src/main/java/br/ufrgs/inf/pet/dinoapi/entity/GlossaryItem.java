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

    @Basic(optional = false)
    @NotNull(message = "Texto não pode ser nulo.")
    @Size(min = 0, max = 1000, message = "O texto deve conter entre 0 e 1000 caracteres.")
    @Column(name = "text", length = 1000)
    private String text;

    @Basic(optional = false)
    @NotNull(message = "Dado de existencia não pode ser nulo.")
    @Column(name = "exists")
    private Boolean exists;

    public GlossaryItem() {}

    public void setByGlossarySaveModel(GlossaryItemSaveModel glossaryItemSaveModel) {
        this.title = glossaryItemSaveModel.getTitle();
        this.text = glossaryItemSaveModel.getText();
        this.exists = true;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getText() {
        return text;
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

        if (!this.text.equals(updateModel.getText())) {
            this.text = updateModel.getText();
            updated = true;
        }

        if (!this.title.equals(updateModel.getTitle())) {
            this.title = updateModel.getTitle();
            updated = true;
        }

        if (this.exists != updateModel.getExists()) {
            this.exists = updateModel.getExists();
            updated = true;
        }

        return updated;
    }
}
