package br.ufrgs.inf.pet.dinoapi.controller.google_owner;

import org.springframework.http.ResponseEntity;

public interface GoogleOwnerController {
    /**
     * Busca o código para verificação de dominio do Google
     *
     * @return retorna o código dado pelo Google
     */
    ResponseEntity<String> get();
}
