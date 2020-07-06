package br.ufrgs.inf.pet.dinoapi.model.glossary;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GlossaryItemSaveModel {
    private String title;
    private String text;

    public GlossaryItemSaveModel() {}

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    @JsonIgnore
    public Boolean isValid() {
        return this.text != null && this.title != null && this.title != "";
    }

}
