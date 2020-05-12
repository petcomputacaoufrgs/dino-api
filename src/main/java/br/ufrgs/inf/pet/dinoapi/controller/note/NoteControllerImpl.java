package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;
import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import br.ufrgs.inf.pet.dinoapi.service.note.NoteServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.note.NoteTagServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.note.NoteVersionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note/")
public class NoteControllerImpl implements NoteController {

    @Autowired
    NoteServiceImpl noteService;

    @Autowired
    NoteTagServiceImpl noteTagService;

    @Autowired
    NoteVersionServiceImpl noteVersionService;

    @Override
    @GetMapping
    public ResponseEntity<List<NoteModel>> getUserNotes() {
        return noteService.getUserNotes();
    }

    @Override
    @PostMapping
    public ResponseEntity<?> saveNewNote(@RequestBody NoteSaveModel model) {
        return noteService.saveNewNote(model);
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Integer> deleteNote(@RequestBody NoteDeleteModel model) {
        return noteService.deleteNote(model);
    }

    @Override
    @PutMapping("all/")
    public ResponseEntity<?> updateAll(@RequestBody List<NoteUpdateModel> models) {
        return noteService.updateAll(models);
    }

    @Override
    @PutMapping("order/")
    public ResponseEntity<?> updateNotesOrder(@RequestBody List<NoteOrderModel> models) {
        return noteService.updateNotesOrder(models);
    }

    @Override
    @PutMapping("question/")
    public ResponseEntity<?> updateNoteQuestion(@RequestBody NoteQuestionModel model) {
        return noteService.updateNoteQuestion(model);
    }

    @Override
    @PutMapping("answer/")
    public ResponseEntity<?> updateNoteAnswer(@RequestBody NoteAnswerModel model) {
        return noteService.updateNoteAnswer(model);
    }

    @Override
    @GetMapping("tags/")
    public ResponseEntity<List<NoteTag>> getTags() {
        return noteTagService.getTags();
    }

    @Override
    @GetMapping("version/")
    public ResponseEntity<Long> getVersion() {
        return noteVersionService.getVersion();
    }
}
