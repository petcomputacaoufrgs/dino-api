package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.model.note.NoteColumnDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteColumnRepository;
import br.ufrgs.inf.pet.dinoapi.service.note.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/note_column/")
public class NoteColumnControllerImpl extends SynchronizableControllerImpl<
        NoteColumn, Long, NoteColumnDataModel, NoteColumnRepository, NoteColumnServiceImpl>  {

    @Autowired
    public NoteColumnControllerImpl(NoteColumnServiceImpl noteColumnService) {
        super(noteColumnService);
    }
}
