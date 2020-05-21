package br.ufrgs.inf.pet.dinoapi.model.glossary;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GlossaryItemUpdateModel {
    private Long id;
    private String title;
    private String subtitle;
    private String text;
    private String fullText;
    private Boolean exists;

    public GlossaryItemUpdateModel() {}

    public Long getId() {
        return id;
    }

    public String getTitle() { return title; }

    public String getSubtitle() { return subtitle; }

    public String getText() { return text; }

    public String getFullText() { return fullText; }

    public Boolean getExists() { return exists; }

    @JsonIgnore
    public Boolean isValid() { return this.text != null && this.title != null && this.title != ""; }

}
