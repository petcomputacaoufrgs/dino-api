package br.ufrgs.inf.pet.dinoapi.model.glossary_item;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Model para recebimento de novo item do glossário
 *
 * @author joao.silva
 */
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
