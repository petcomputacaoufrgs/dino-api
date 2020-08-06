package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoteTagService {

    ResponseEntity<List<NoteTag>> getTags();
}
