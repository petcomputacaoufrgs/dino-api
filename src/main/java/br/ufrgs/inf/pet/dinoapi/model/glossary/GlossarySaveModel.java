package br.ufrgs.inf.pet.dinoapi.model.glossary;

import java.util.ArrayList;
import java.util.List;

public class GlossarySaveModel {

    private List<GlossaryItemSaveModel> itemList;

    public GlossarySaveModel(){
        itemList = new ArrayList<>();
    }

    public List<GlossaryItemSaveModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<GlossaryItemSaveModel> itemList) {
        this.itemList = itemList;
    }

    public void addItem(GlossaryItemSaveModel item) {
        this.itemList.add(item);
    }

}
