package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import br.ufrgs.inf.pet.dinoapi.model.notes.sync.NoteColumnSyncRequestModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoteColumnService {

    ResponseEntity<List<NoteColumnResponseModel>> getUserColumns();

    ResponseEntity<?> save(NoteColumnSaveRequestModel model);

    ResponseEntity<Long> deleteAll(List<NoteColumnDeleteRequestModel> models);

    ResponseEntity<Long> delete(NoteColumnDeleteRequestModel model);

    ResponseEntity<?> sync(NoteColumnSyncRequestModel model);

    ResponseEntity<?> updateOrder(List<NoteColumnOrderRequestModel> models);

    List<NoteColumn> findAllByUserIdAndTitles(List<String> titles, Long userId);

    NoteColumn findOneOrCreateByUserAndTitle(String title, User user);

    Integer getMaxOrder(Long userId);

    NoteColumn save(NoteColumn noteColumn);

}
