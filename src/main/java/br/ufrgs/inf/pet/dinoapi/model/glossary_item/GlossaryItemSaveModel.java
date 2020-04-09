package br.ufrgs.inf.pet.dinoapi.model.glossary_item;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Model para recebimento de novo item do glossário
 *
 * @author joao.silva
 */
public class GlossaryItemSaveModel {
    String title;
    String text;
    Boolean exists;

    public GlossaryItemSaveModel() {}

    public GlossaryItemSaveModel(Long id, String title, String text, Boolean exists) {
        this.title = title;
        this.text = text;
        this.exists = exists;
    }

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

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }
}
