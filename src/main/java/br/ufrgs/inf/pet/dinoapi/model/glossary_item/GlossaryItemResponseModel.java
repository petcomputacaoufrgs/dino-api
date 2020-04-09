package br.ufrgs.inf.pet.dinoapi.model.glossary_item;

import br.ufrgs.inf.pet.dinoapi.entity.GlossaryItem;

/**
 * Model para envio de um item do glossário
 *
 * @author joao.silva
 */
public class GlossaryItemResponseModel {
    Long id;
    String title;
    String text;

    public GlossaryItemResponseModel() {}

    public void setByGlossaryItem(GlossaryItem glossaryItem) {
        this.id = glossaryItem.getId();
        this.title = glossaryItem.getTitle();
        this.text = glossaryItem.getText();
    }
}
