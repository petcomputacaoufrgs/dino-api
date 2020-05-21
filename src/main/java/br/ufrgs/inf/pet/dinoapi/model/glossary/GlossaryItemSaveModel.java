package br.ufrgs.inf.pet.dinoapi.model.glossary;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GlossaryItemSaveModel {
    private String title;
    private String subtitle;
    private String text;
    private String full_text;

    public GlossaryItemSaveModel() {}

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getText() {
        return text;
    }

    public String getFullText() {
        return full_text;
    }

    @JsonIgnore
    public Boolean isValid() {
        return this.text != null && this.title != null && this.title != "";
    }

}
