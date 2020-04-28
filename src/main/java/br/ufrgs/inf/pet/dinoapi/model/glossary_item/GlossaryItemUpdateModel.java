package br.ufrgs.inf.pet.dinoapi.model.glossary_item;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GlossaryItemUpdateModel {
    private Long id;
    private String title;
    private String text;
    private Boolean exists;

    public GlossaryItemUpdateModel() {}

    public Long getId() {
        return id;
    }

    public String getTitle() { return title; }

    public String getText() { return text; }

    public Boolean getExists() { return exists; }

    @JsonIgnore
    public Boolean isValid() {
        return this.text != null && this.title != null && this.title != "";
    }

}
