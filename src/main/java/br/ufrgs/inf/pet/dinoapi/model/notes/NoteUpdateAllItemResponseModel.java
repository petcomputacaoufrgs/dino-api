package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;

public class NoteUpdateAllItemResponseModel {
    private String question;
    private Long id;

    public NoteUpdateAllItemResponseModel(Note note) {
        this.question = note.getQuestion();
        this.id = note.getId();
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
