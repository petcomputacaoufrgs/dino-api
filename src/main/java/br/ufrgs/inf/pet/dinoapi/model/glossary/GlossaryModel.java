package br.ufrgs.inf.pet.dinoapi.model.glossary;

import br.ufrgs.inf.pet.dinoapi.model.glossary_item.GlossaryItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Model para recebimento de novo glossário (lista de itens)
 *
 * @author joao.silva
 */
public class GlossaryModel {

    Long version;

    List<GlossaryItemModel> itemList;

    public GlossaryModel(){
        itemList = new ArrayList<>();
    }

    public List<GlossaryItemModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<GlossaryItemModel> itemList) {
        this.itemList = itemList;
    }

    public void addItem(GlossaryItemModel item) {
        this.itemList.add(item);
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
