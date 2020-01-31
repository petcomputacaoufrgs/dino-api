package br.ufrgs.inf.pet.dinoapi.model.glossary_item_model;

/**
 * Model para recebimento de novo item do glossário
 *
 * @author joao.silva
 */
public class GlossaryItemModel {
    Long id;
    String title;
    String text;

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Boolean isValid() {
        return this.text != null && this.title != null && this.title != "";
    }

    public void setId(Long id) {
        this.id = id;
    }
}
