package br.ufrgs.inf.pet.dinoapi.controller.google_owner;

import org.springframework.http.ResponseEntity;

public interface GoogleOwnerController {
    /**
     * @return retorna o código
     */
    ResponseEntity<String> get();
}
