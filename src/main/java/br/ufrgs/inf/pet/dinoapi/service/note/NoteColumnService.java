package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoteColumnService {

    ResponseEntity<List<NoteColumnResponseModel>> getUserColumns();

    ResponseEntity<?> save(NoteColumnSaveRequestModel model);

    ResponseEntity<Long> deleteAll(List<NoteColumnDeleteRequestModel> models);

    ResponseEntity<Long> delete(NoteColumnDeleteRequestModel model);

    ResponseEntity<Long> updateAll(List<NoteColumnSaveRequestModel> models);

    ResponseEntity<?> updateOrder(List<NoteColumnOrderRequestModel> models);

    List<NoteColumn> findAllByUserIdAndTitle(List<String> titles, Long userId);

    NoteColumn findOneByUserIdAndTitle(String title, Long userId);

    Integer getMaxOrder(Long userId);

    NoteColumn save(NoteColumn noteColumn);

}
