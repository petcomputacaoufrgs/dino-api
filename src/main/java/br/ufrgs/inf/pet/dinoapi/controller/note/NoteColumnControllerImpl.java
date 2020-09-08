package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import br.ufrgs.inf.pet.dinoapi.service.note.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note_column/")
public class NoteColumnControllerImpl implements NoteColumnController {

    private final NoteColumnServiceImpl noteColumnService;

    private final NoteColumnVersionServiceImpl noteVersionService;

    @Autowired
    public NoteColumnControllerImpl(NoteColumnServiceImpl noteColumnService, NoteColumnVersionServiceImpl noteVersionService) {
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
    public ResponseEntity<?> save(NoteColumnSaveRequestModel model) {
        return noteColumnService.save(model);
    }

    @Override
    @DeleteMapping("all/")
    public ResponseEntity<Long> deleteAll(List<NoteColumnDeleteRequestModel> models) {
        return noteColumnService.deleteAll(models);
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Long> delete(NoteColumnDeleteRequestModel model) {
        return noteColumnService.delete(model);
    }

    @Override
    @PutMapping("all/")
    public ResponseEntity<Long> updateAll(List<NoteColumnSaveRequestModel> models) {
        return noteColumnService.updateAll(models);
    }

    @Override
    @PutMapping("order/")
    public ResponseEntity<?> updateOrder(List<NoteColumnOrderRequestModel> models) {
        return noteColumnService.updateOrder(models);
    }

    @Override
    @GetMapping("version/")
    public ResponseEntity<Long> getVersion() {
        return noteVersionService.getVersion();
    }
}
