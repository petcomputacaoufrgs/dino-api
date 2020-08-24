package br.ufrgs.inf.pet.dinoapi.model.faq;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static br.ufrgs.inf.pet.dinoapi.constants.FaqConstants.*;

public class FaqSaveRequestModel {

    @NotNull(message = TITLE_NULL_MESSAGE)
    @Size(min = 1, max = TITLE_MAX, message = TITLE_MAX_MESSAGE)
    private String title;

    @Valid
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
