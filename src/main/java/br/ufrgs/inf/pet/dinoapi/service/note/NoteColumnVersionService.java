package br.ufrgs.inf.pet.dinoapi.service.note;

import org.springframework.http.ResponseEntity;

public interface NoteColumnVersionService {

    ResponseEntity<Long> getVersion();

    Long updateVersion();

}
