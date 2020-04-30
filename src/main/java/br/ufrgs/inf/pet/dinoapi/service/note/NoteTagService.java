package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;
import br.ufrgs.inf.pet.dinoapi.model.notes.NoteTagModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoteTagService {
    /**
     * Retorna todas as tags salvas relacionadas a anotações do usuário
     *
     * @return Lista com as tags no formato {@link NoteTagModel}
     **/
    ResponseEntity<List<NoteTag>> getTags();
}
