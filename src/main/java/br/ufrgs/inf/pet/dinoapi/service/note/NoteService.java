package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface NoteService {

    ResponseEntity<List<NoteResponseModel>> getUserNotes();

    ResponseEntity<?> saveNote(NoteSaveRequestModel model);

    ResponseEntity<Long> deleteAll(List<NoteDeleteRequestModel> models);

    ResponseEntity<Long> deleteNote(NoteDeleteRequestModel model);

    ResponseEntity<?> updateAll(List<NoteSaveRequestModel> models);

    ResponseEntity<?> updateNotesOrder(List<NoteOrderRequestModel> models);

}
