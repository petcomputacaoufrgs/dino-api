package br.ufrgs.inf.pet.dinoapi.model.glossary_model;

import br.ufrgs.inf.pet.dinoapi.model.glossary_item_model.GlossaryItemModel;

import java.util.List;

/**
 * Model para recebimento de novo glossário (lista de itens)
 *
 * @author joao.silva
 */
public class GlossaryModel {

    List<GlossaryItemModel> itemList;

    public List<GlossaryItemModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<GlossaryItemModel> itemList) {
        this.itemList = itemList;
    }
}
