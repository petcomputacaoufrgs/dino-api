package br.ufrgs.inf.pet.dinoapi.model.glossary_item;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Model para recebimento de atualização de item do glossário
 *
 * @author joao.silva
 */
public class GlossaryItemUpdateModel {
    Long id;
    String title;
    String text;
    Boolean exists;

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
