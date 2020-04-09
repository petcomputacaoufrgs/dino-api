package br.ufrgs.inf.pet.dinoapi.model.glossary;

import br.ufrgs.inf.pet.dinoapi.model.glossary_item.GlossaryItemResponseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Model para envio de resposta para criação e atualização de itens do glossário
 *
 * @author joao.silva
 */
public class GlossaryResponseModel {

    private Long version;

    private List<GlossaryItemResponseModel> itemList;

    public GlossaryResponseModel(){
        itemList = new ArrayList<>();
    }

    public void addItem(GlossaryItemResponseModel item) {
        this.itemList.add(item);
    }

    public int getSize() { return this.itemList.size(); }

    public void setVersion(Long version) {
        this.version = version;
    }

}
