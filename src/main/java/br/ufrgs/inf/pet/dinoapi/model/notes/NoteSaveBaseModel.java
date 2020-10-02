package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

public class NoteSaveBaseModel {
    @Valid
    @Size(max = NoteConstants.MAX_TAGS, message = NoteConstants.MAX_TAGS_MESSAGE)
    protected List<String> tagNames;

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }
}
