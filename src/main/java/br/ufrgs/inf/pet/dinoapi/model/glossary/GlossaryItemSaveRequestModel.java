package br.ufrgs.inf.pet.dinoapi.model.glossary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GlossaryItemSaveRequestModel {

    @NotNull(message = "title can't be null.")
    @Size(max = 100, message = "title should not be more than 500.")
    private String title;

    @NotNull(message = "text can't be null.")
    @Size(max = 1000, message = "text should not be more than 500.")
    private String text;

    @Size(max = 20, message = "text should not be more than 20.")
    private String subtitle;

    @Size(max = 20000, message = "text should not be more than 20000.")
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
