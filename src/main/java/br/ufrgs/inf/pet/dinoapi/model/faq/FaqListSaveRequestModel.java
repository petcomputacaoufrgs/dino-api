package br.ufrgs.inf.pet.dinoapi.model.faq;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class FaqListSaveRequestModel {
    @Valid
    private List<FaqSaveRequestModel> items;

    public FaqListSaveRequestModel() {
        items = new ArrayList<>();
    }

    public List<FaqSaveRequestModel> getItems() {
        return items;
    }

    public void setItems(List<FaqSaveRequestModel> items) {
        this.items = items;
    }
}
