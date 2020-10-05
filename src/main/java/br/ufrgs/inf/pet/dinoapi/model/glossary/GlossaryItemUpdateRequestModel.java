package br.ufrgs.inf.pet.dinoapi.model.glossary;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.GlossaryConstants.*;

public class GlossaryItemUpdateRequestModel {

    @NotNull(message = ITEM_ID_NULL_MESSAGE)
    private Long id;

    @NotNull(message = TITLE_NULL_MESSAGE)
    @Size(max = TITLE_MAX, message = TITLE_MESSAGE)
    private String title;

    //@NotNull(message = TEXT_NULL_MESSAGE)
    @Size(max = TEXT_MAX, message = TEXT_MESSAGE)
    private String text;

    @Size(max = SUBTITLE_MAX, message = SUBTITLE_MESSAGE)
    private String subtitle;

    @Size(max = FULLTEXT_MAX, message = FULLTEXT_MESSAGE)
    private String fullText;

    @NotNull(message = EXISTS_NULL_MESSAGE)
    private Boolean exists;

    public GlossaryItemUpdateRequestModel() {}

    public Long getId() {
        return id;
    }

    public String getTitle() { return title; }

    public String getText() { return text; }

    public Boolean getExists() { return exists; }

    public String getSubtitle() {
        return subtitle;
    }

    public String getFullText() {
        return fullText;
    }

    @JsonIgnore
    public Boolean isValid() {
        return this.text != null && this.title != null && this.title != "";
    }

}
