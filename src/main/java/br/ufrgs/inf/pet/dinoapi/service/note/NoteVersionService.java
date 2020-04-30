package br.ufrgs.inf.pet.dinoapi.service.note;

import org.springframework.http.ResponseEntity;

public interface NoteVersionService {
    /**
     * Retorna a versão das anotações do usuário
     **/
    ResponseEntity<Long> getVersion();
}
