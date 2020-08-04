package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface NoteService {

    ResponseEntity<List<NoteResponseModel>> getUserNotes();

    ResponseEntity<?> saveNewNote(NoteSaveRequestRequestModel model);

    ResponseEntity<Long> deleteAll(List<NoteDeleteRequestModel> models);

    ResponseEntity<Long> deleteNote(NoteDeleteRequestModel model);

    ResponseEntity<?> saveAll(List<NoteSaveRequestRequestModel> models);

    ResponseEntity<?> updateAll(List<NoteUpdateRequestModel> models);

    ResponseEntity<?> updateNotesOrder(List<NoteOrderRequestModel> models);

    ResponseEntity<?> updateNoteQuestion(NoteQuestionRequestModel model);

    ResponseEntity<?> updateNoteAnswer(NoteAnswerRequestModel model);

}
