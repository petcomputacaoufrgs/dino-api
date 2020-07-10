package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.NoteVersion;
import org.springframework.http.ResponseEntity;

public interface NoteVersionService {
    /**
     * Retorna a versão das anotações do usuário
     **/
    ResponseEntity<Long> getVersion();

    /**
     * Atualiza a versão das anotações do usuário
     **/
    Long updateVersion();
}
