package br.ufrgs.inf.pet.dinoapi.model.glossary;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static br.ufrgs.inf.pet.dinoapi.constants.GlossaryConstants.ITEM_LIST_NULL_MESSAGE;
import static br.ufrgs.inf.pet.dinoapi.constants.GlossaryConstants.VERSION_NULL_MESSAGE;

public class GlossaryUpdateRequestModel {

    @NotNull(message = VERSION_NULL_MESSAGE)
    private Long version;

    @Valid
    @NotNull(message = ITEM_LIST_NULL_MESSAGE)
    private List<GlossaryItemUpdateRequestModel> itemList;

    public GlossaryUpdateRequestModel(){
        this.itemList = new ArrayList<>();
    }

    public Long getVersion() { return version; }

    public List<GlossaryItemUpdateRequestModel> getItemList() {
        return itemList;
    }

}
