package br.ufrgs.inf.pet.dinoapi.entity.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static br.ufrgs.inf.pet.dinoapi.constants.GlossaryConstants.*;

@Entity
@Table(name = "glossary_item")
public class GlossaryItem extends SynchronizableEntity<Long> {
    @Column(name = "title", length = TITLE_MAX, nullable = false, unique = true)
    private String title;

    @Column(name = "text", length = TEXT_MAX, nullable = false)
    private String text;

    @Column(name = "subtitle", length = SUBTITLE_MAX)
    private String subtitle;

    @Column(name = "full_text", length = FULLTEXT_MAX)
    private String fullText;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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


