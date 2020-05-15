package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface NoteService {

    ResponseEntity<List<NoteModel>> getUserNotes();

    ResponseEntity<?> saveNewNote(NoteSaveModel model);

    ResponseEntity<Long> deleteAll(List<NoteDeleteModel> models);

    ResponseEntity<Long> deleteNote(NoteDeleteModel model);

    ResponseEntity<?> saveAll(List<NoteSaveModel> models);

    ResponseEntity<?> updateAll(List<NoteUpdateModel> models);

    ResponseEntity<?> updateNotesOrder(List<NoteOrderModel> models);

    ResponseEntity<?> updateNoteQuestion(NoteQuestionModel model);

    ResponseEntity<?> updateNoteAnswer(NoteAnswerModel model);

}
