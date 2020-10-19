package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.model.note.delete.NoteColumnDeleteAllRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.delete.NoteColumnDeleteRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.get.NoteColumnResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.note.order.NoteColumnOrderAllRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.save.NoteColumnSaveRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.sync.column.NoteColumnSyncRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.sync.column.NoteColumnSyncResponse;
import br.ufrgs.inf.pet.dinoapi.service.note.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/note_column/")
public class NoteColumnControllerImpl implements NoteColumnController {

    private final NoteColumnServiceImpl noteColumnService;

    private final NoteVersionServiceImpl noteVersionService;

    @Autowired
    public NoteColumnControllerImpl(NoteColumnServiceImpl noteColumnService, NoteVersionServiceImpl noteVersionService) {
        this.noteColumnService = noteColumnService;
        this.noteVersionService = noteVersionService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<NoteColumnResponseModel>> getUserColumns() {
        return noteColumnService.getUserColumns();
    }

    @Override
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody NoteColumnSaveRequestModel model) {
        return noteColumnService.save(model);
    }

    @Override
    @DeleteMapping("all/")
    public ResponseEntity<Long> deleteAll(@Valid @RequestBody NoteColumnDeleteAllRequestModel model) {
        return noteColumnService.deleteAll(model.getItems());
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Long> delete(@Valid @RequestBody NoteColumnDeleteRequestModel model) {
        return noteColumnService.delete(model);
    }

    @Override
    @PutMapping("sync/")
    public ResponseEntity<NoteColumnSyncResponse> sync(@Valid @RequestBody NoteColumnSyncRequestModel model) {
        return noteColumnService.sync(model);
    }

    @Override
    @PutMapping("order/")
    public ResponseEntity<?> updateOrder(@Valid @RequestBody NoteColumnOrderAllRequestModel model) {
        return noteColumnService.updateOrder(model.getItems());
    }

    @Override
    @GetMapping("version/")
    public ResponseEntity<Long> getVersion() {
        return noteVersionService.getNoteColumnVersion();
    }
}
