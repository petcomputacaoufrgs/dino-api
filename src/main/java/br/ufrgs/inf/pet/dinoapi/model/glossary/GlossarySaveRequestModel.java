package br.ufrgs.inf.pet.dinoapi.model.glossary;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class GlossarySaveRequestModel {

    @Valid
    @NotNull(message = "itemList cannot be null.")
    private List<GlossaryItemSaveRequestModel> itemList;

    public GlossarySaveRequestModel(){
        this.itemList = new ArrayList<>();
    }

    public List<GlossaryItemSaveRequestModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<GlossaryItemSaveRequestModel> itemList) {
        this.itemList = itemList;
    }

    public void addItem(GlossaryItemSaveRequestModel item) {
        this.itemList.add(item);
    }

}
