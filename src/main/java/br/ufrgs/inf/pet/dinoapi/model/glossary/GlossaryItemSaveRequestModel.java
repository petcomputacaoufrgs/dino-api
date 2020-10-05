package br.ufrgs.inf.pet.dinoapi.model.glossary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.GlossaryConstants.*;

public class GlossaryItemSaveRequestModel {

    @NotNull(message = TITLE_NULL_MESSAGE)
    @Size(min = 1, max = TITLE_MAX, message = TITLE_MESSAGE)
    private String title;

    //@NotNull(message = TEXT_NULL_MESSAGE)
    @Size(max = TEXT_MAX, message = TEXT_MESSAGE)
    private String text;

    @Size(max = SUBTITLE_MAX, message = SUBTITLE_MESSAGE)
    private String subtitle;

    @Size(max = FULLTEXT_MAX, message = FULLTEXT_MESSAGE)
    private String fullText;

    public GlossaryItemSaveRequestModel() {}

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

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
