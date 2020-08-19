package br.ufrgs.inf.pet.dinoapi.model.faq;

import java.util.List;

public class FaqSaveRequestModel {

    private String title;
    private List<FaqSaveRequestItemModel> items;

    public FaqSaveRequestModel(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FaqSaveRequestItemModel> getItems() {
        return items;
    }

    public void setItems(List<FaqSaveRequestItemModel> items) {
        this.items = items;
    }


}
