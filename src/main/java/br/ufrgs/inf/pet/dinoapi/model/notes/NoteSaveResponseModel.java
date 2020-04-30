package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.Note;
import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;

import java.util.List;
import java.util.stream.Collectors;

public class NoteSaveResponseModel {
    Long version;

    Long noteId;

    List<NoteTagModel> newTags;

    public NoteSaveResponseModel() {}

    public NoteSaveResponseModel(Long version, List<NoteTag> tags, Long noteId) {
        this.version = version;
        this.newTags = tags.stream().map(tag -> new NoteTagModel(tag)).collect(Collectors.toList());
        this.noteId = noteId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<NoteTagModel> getNewTags() {
        return newTags;
    }

    public void setNewTags(List<NoteTagModel> newTags) {
        this.newTags = newTags;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }
}
