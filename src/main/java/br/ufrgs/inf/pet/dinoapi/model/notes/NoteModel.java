package br.ufrgs.inf.pet.dinoapi.model.notes;

import java.util.List;

public class NoteModel {
    Long id;

    Integer order;

    String question;

    String answer;

    Boolean answered;

    List<NoteTagModel> tagList;

    Integer creationDay;

    Integer creationMonth;

    Integer creationYear;

    Boolean savedOnServer;
}
