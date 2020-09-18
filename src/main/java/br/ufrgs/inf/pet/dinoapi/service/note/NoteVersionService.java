package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoteVersionService {

    ResponseEntity<Long> getNoteVersion();

    ResponseEntity<Long> getNoteColumnVersion();

    Long updateNoteVersion();

    Long updateColumnVersion();

    Long updateColumnVersionTitle(String newTitle, String oldTitle) throws JsonProcessingException;

    Long updateColumnVersionOrder(List<NoteColumn> columnList);

    Long updateColumnVersionDelete(String title);

    Long updateColumnVersionDelete(List<String> titleList);

}
