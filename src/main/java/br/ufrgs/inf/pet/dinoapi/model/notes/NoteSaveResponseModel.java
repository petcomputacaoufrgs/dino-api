package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;

import java.util.List;
import java.util.stream.Collectors;

public class NoteSaveResponseModel {
    Long version;

    List<NoteTagModel> tags;

    public NoteSaveResponseModel() {}

    public NoteSaveResponseModel(Long version, List<NoteTag> tags) {
        this.version = version;
        this.tags = tags.stream().map(tag -> new NoteTagModel(tag)).collect(Collectors.toList());
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<NoteTagModel> getTags() {
        return tags;
    }

    public void setTags(List<NoteTagModel> tags) {
        this.tags = tags;
    }

}
