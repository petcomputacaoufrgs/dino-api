package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import br.ufrgs.inf.pet.dinoapi.model.note.NoteDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteRepository;
import br.ufrgs.inf.pet.dinoapi.service.note.NoteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/note/")
public class NoteControllerImpl extends SynchronizableControllerImpl<
        Note, Long, Integer, NoteDataModel, NoteRepository, NoteServiceImpl> {

    @Autowired
    public NoteControllerImpl(NoteServiceImpl noteService) {
        super(noteService);
    }
}