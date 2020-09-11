package br.ufrgs.inf.pet.dinoapi.entity;

import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryItemSaveRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryItemUpdateRequestModel;
import javax.persistence.*;

import static br.ufrgs.inf.pet.dinoapi.constants.GlossaryConstants.*;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "glossary_item")
public class GlossaryItem {

    private static final String SEQUENCE_NAME = "glossary_item_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", length = TITLE_MAX, nullable = false, unique = true)
    private String title;

    @Column(name = "text", length = TEXT_MAX, nullable = false)
    private String text;

    @Column(name = "subtitle", length = SUBTITLE_MAX)
    private String subtitle;

    @Column(name = "full_text", length = FULLTEXT_MAX)
    private String fullText;

    @Column(name = "exists", nullable = false)
    private Boolean exists;

    public GlossaryItem() {}

    public void GlossaryItem(GlossaryItemSaveRequestModel glossaryItemSaveModel) {
        this.title = glossaryItemSaveModel.getTitle();
        this.subtitle = glossaryItemSaveModel.getSubtitle();
        this.text = glossaryItemSaveModel.getText();
        this.fullText = glossaryItemSaveModel.getFullText();
        this.exists = true;
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
        return fullText;
    }

    public Boolean getExists() {
        return exists;
    }

    public Boolean update(GlossaryItemUpdateRequestModel updateModel) {
        Boolean updated = false;

        if (!this.title.equals(updateModel.getTitle())) {
            this.title = updateModel.getTitle();
            updated = true;
        }

        if (!this.subtitle.equals(updateModel.getSubtitle())) {
            this.subtitle = updateModel.getSubtitle();
            updated = true;
        }

        if (!this.text.equals(updateModel.getText())) {
            this.text = updateModel.getText();
            updated = true;
        }

        if(this.fullText == null || !this.fullText.equals(updateModel.getFullText())) {
            this.fullText = updateModel.getFullText();
            updated = true;
        }

        if (this.exists != updateModel.getExists()) {
            this.exists = updateModel.getExists();
            updated = true;
        }

        return updated;
    }
}


