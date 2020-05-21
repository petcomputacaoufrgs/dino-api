package br.ufrgs.inf.pet.dinoapi.model.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.GlossaryItem;

public class GlossaryItemResponseModel {
    private Long id;
    private String title;
    private String subtitle;
    private String text;
    private String full_text;
    private Boolean exists;

    public GlossaryItemResponseModel() {}

    public void setByGlossaryItem(GlossaryItem glossaryItem) {
        this.id = glossaryItem.getId();
        this.title = glossaryItem.getTitle();
        this.subtitle = glossaryItem.getSubtitle();
        this.text = glossaryItem.getText();
        this.full_text = glossaryItem.getFullText();
        this.exists = glossaryItem.getExists();
    }

    public Long getId() {
        return id;
    }

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

    public Boolean getExists() {
        return exists;
    }
}
