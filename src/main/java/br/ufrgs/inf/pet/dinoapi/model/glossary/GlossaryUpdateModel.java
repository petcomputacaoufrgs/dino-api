package br.ufrgs.inf.pet.dinoapi.model.glossary;

import br.ufrgs.inf.pet.dinoapi.model.glossary_item.GlossaryItemUpdateModel;

import java.util.ArrayList;
import java.util.List;

public class GlossaryUpdateModel {

    private Long version;

    private List<GlossaryItemUpdateModel> itemList;

    public GlossaryUpdateModel(){
        itemList = new ArrayList<>();
    }

    public Long getVersion() { return version; }

    public List<GlossaryItemUpdateModel> getItemList() {
        return itemList;
    }

}
