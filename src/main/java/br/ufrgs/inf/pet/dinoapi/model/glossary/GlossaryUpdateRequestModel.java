package br.ufrgs.inf.pet.dinoapi.model.glossary;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class GlossaryUpdateRequestModel {

    @NotNull(message = "version cannot be null.")
    private Long version;

    @Valid
    @NotNull(message = "itemList cannot be null.")
    private List<GlossaryItemUpdateRequestModel> itemList;

    public GlossaryUpdateRequestModel(){
        this.itemList = new ArrayList<>();
    }

    public Long getVersion() { return version; }

    public List<GlossaryItemUpdateRequestModel> getItemList() {
        return itemList;
    }

}
