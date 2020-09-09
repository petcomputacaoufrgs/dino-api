package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.model.notes.*;
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
    public ResponseEntity<?> save(@Valid NoteColumnSaveRequestModel model) {
        return noteColumnService.save(model);
    }

    @Override
    @DeleteMapping("all/")
    public ResponseEntity<Long> deleteAll(@Valid NoteColumnDeleteAllRequestModel model) {
        return noteColumnService.deleteAll(model.getItems());
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Long> delete(@Valid NoteColumnDeleteRequestModel model) {
        return noteColumnService.delete(model);
    }

    @Override
    @PutMapping("all/")
    public ResponseEntity<Long> updateAll(@Valid NoteColumnUpdateAllRequestModel model) {
        return noteColumnService.updateAll(model.getItems());
    }

    @Override
    @PutMapping("order/")
    public ResponseEntity<?> updateOrder(@Valid NoteColumnOrderAllRequestModel model) {
        return noteColumnService.updateOrder(model.getItems());
    }

    @Override
    @GetMapping("version/")
    public ResponseEntity<Long> getVersion() {
        return noteVersionService.getVersion();
    }
}
