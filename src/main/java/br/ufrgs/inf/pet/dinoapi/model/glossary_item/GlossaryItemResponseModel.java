package br.ufrgs.inf.pet.dinoapi.model.glossary_item;

import br.ufrgs.inf.pet.dinoapi.entity.GlossaryItem;

/**
 * Model para envio de um item do glossário
 *
 * @author joao.silva
 */
public class GlossaryItemResponseModel {
    private Long id;
    private String title;
    private String text;
    private Boolean exists;

    public GlossaryItemResponseModel() {}

    public void setByGlossaryItem(GlossaryItem glossaryItem) {
        this.id = glossaryItem.getId();
        this.title = glossaryItem.getTitle();
        this.text = glossaryItem.getText();
        this.exists = glossaryItem.getExists();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Boolean getExists() {
        return exists;
    }
}
