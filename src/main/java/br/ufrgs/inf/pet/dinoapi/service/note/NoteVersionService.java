package br.ufrgs.inf.pet.dinoapi.service.note;

import org.springframework.http.ResponseEntity;

public interface NoteVersionService {

    ResponseEntity<Long> getVersion();

    Long updateVersion();

    Long updateColumnVersion();
}
