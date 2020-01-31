package br.ufrgs.inf.pet.dinoapi.entity;

import br.ufrgs.inf.pet.dinoapi.model.glossary_item.GlossaryItemModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Classe de persistencia para a tabela com os itens do glossário no banco de dados
 *
 * @author joao.silva
 */
@Entity
@Table(name = "glossary_item")
public class GlossaryItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "glossary_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "glossary_item_id")
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

    public GlossaryItem(String title, String text) {
        this.title = title;
        this.text = text;
        this.exists = true;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    /**
     * Atualiza o GlossaryItem baseado na {@link GlossaryItemModel} se houverem mudanças aplicaveis.
     * Só atualiza se os títulos forem iguais!
     * @param model {@link GlossaryItemModel} com os novos dados
     * @return True se houver modificações e False se não houver modificações
     */
    public Boolean update(GlossaryItemModel model) {
        Boolean updated = false;
        if (this.title.equals(model.getTitle())){
            if (!this.text.equals(model.getText())){
                this.text = model.getText();
                updated = true;
            }
            if (this.exists != model.getExists()) {
                this.exists = model.getExists();
                updated = true;
            }
        }
        return updated;
    }
}
