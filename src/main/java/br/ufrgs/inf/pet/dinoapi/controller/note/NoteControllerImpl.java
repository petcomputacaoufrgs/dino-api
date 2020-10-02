package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteTag;
import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import br.ufrgs.inf.pet.dinoapi.model.notes.sync.note.NoteSyncRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.notes.sync.note.NoteSyncResponseModel;
import br.ufrgs.inf.pet.dinoapi.service.note.NoteServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.note.NoteTagServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.note.NoteVersionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/note/")
public class NoteControllerImpl implements NoteController {

    private final NoteServiceImpl noteService;

    private final NoteTagServiceImpl noteTagService;

    private final NoteVersionServiceImpl noteVersionService;

    @Autowired
    public NoteControllerImpl(NoteServiceImpl noteService, NoteTagServiceImpl noteTagService, NoteVersionServiceImpl noteVersionService) {
        this.noteService = noteService;
        this.noteTagService = noteTagService;
        this.noteVersionService = noteVersionService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<NoteResponseModel>> getUserNotes() {
        return noteService.getUserNotes();
    }

    @Override
    @PostMapping
    public ResponseEntity<?> saveNote(@Valid @RequestBody NoteSaveRequestModel model) {
        return noteService.saveNote(model);
    }

    @Override
    @DeleteMapping("all/")
    public ResponseEntity<Long> deleteAll(@Valid @RequestBody NoteDeleteAllRequestModel model) {
        return noteService.deleteAll(model.getItems());
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Long> deleteNote(@Valid @RequestBody NoteDeleteRequestModel model) {
        return noteService.deleteNote(model);
    }

    @Override
    @PutMapping("sync/")
    public ResponseEntity<NoteSyncResponseModel> sync(@Valid @RequestBody NoteSyncRequestModel model) {
        return noteService.sync(model);
    }

    @Override
    @PutMapping("order/")
    public ResponseEntity<?> updateNotesOrder(@Valid @RequestBody NoteOrderAllRequestModel model) {
        return noteService.updateNotesOrder(model.getItems());
    }

    @Override
    @GetMapping("tags/")
    public ResponseEntity<List<NoteTag>> getTags() {
        return noteTagService.getTags();
    }

    @Override
    @GetMapping("version/")
    public ResponseEntity<Long> getVersion() {
        return noteVersionService.getNoteVersion();
    }
}
