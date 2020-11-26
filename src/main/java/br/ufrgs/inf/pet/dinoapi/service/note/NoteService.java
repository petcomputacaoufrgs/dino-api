package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.model.note.delete.NoteDeleteRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.get.NoteResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.note.order.NoteOrderRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.save.NoteSaveRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.sync.note.NoteSyncRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.sync.note.NoteSyncResponseModel;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface NoteService {

    ResponseEntity<List<NoteResponseModel>> getUserNotes();

    ResponseEntity<?> saveNote(NoteSaveRequestModel model);

    ResponseEntity<Long> deleteAll(List<NoteDeleteRequestModel> models);

    ResponseEntity<Long> deleteNote(NoteDeleteRequestModel model);

    ResponseEntity<NoteSyncResponseModel> sync(NoteSyncRequestModel model);

    ResponseEntity<?> updateNotesOrder(List<NoteOrderRequestModel> models);
}
