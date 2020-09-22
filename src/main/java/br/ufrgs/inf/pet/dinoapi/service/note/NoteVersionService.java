package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoteVersionService {

    ResponseEntity<Long> getNoteVersion();

    ResponseEntity<Long> getNoteColumnVersion();

    Long updateNoteVersion();

    void updateNoteOrder(List<Note> noteList);

    Long updateNoteVersionDelete(Long id);

    Long updateNoteVersionDelete(List<Long> idList);

    Long updateColumnVersion();

    void updateColumnOrder(List<NoteColumn> columnList);

    Long updateColumnVersionDelete(String title);

    Long updateColumnVersionDelete(List<String> titleList);

}
