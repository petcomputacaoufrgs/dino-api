package br.ufrgs.inf.pet.dinoapi.model.glossary;

import br.ufrgs.inf.pet.dinoapi.model.glossary_item.GlossaryItemUpdateModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Model para recebimento de atualizações para itens do glossário (lista de itens)
 *
 * @author joao.silva
 */
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
