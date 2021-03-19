package br.ufrgs.inf.pet.dinoapi.model.glossary;

import br.ufrgs.inf.pet.dinoapi.constants.GlossaryConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class GlossaryItemDataModel extends SynchronizableDataLocalIdModel<Long> {
    private static String REPEAT_SEPARATOR_START = "[[";
    private static String REPEAT_SEPARATOR_END = "]]R";

    @NotNull(message = GlossaryConstants.TITLE_NULL_MESSAGE)
    @Size(min = GlossaryConstants.TITLE_MIN, max = GlossaryConstants.TITLE_MAX, message = GlossaryConstants.TITLE_MESSAGE)
    private String title;

    @NotNull(message = GlossaryConstants.TEXT_NULL_MESSAGE)
    @Size(max = GlossaryConstants.TEXT_MAX, message = GlossaryConstants.TEXT_MESSAGE)
    private String text;

    @Size(max = GlossaryConstants.SUBTITLE_MAX, message = GlossaryConstants.SUBTITLE_MESSAGE)
    private String subtitle;

    @Size(max = GlossaryConstants.FULLTEXT_MAX, message = GlossaryConstants.FULLTEXT_MESSAGE)
    private String fullText;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title.endsWith(REPEAT_SEPARATOR_END)) {
            title = title.substring(0, title.length() - 1);
        }
        this.title = title;
    }

    public void directSetTitle(String title) {
        this.title = title;
    }

    public void changeRepeatedTitle(Long id) {
        this.title = title + " " + REPEAT_SEPARATOR_START + id.toString() + REPEAT_SEPARATOR_END;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

}
