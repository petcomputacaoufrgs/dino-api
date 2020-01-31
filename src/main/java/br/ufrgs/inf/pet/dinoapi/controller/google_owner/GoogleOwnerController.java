package br.ufrgs.inf.pet.dinoapi.controller.google_owner;

import org.springframework.http.ResponseEntity;

/**
 * Controller retornar o código de Google owner
 * @author joao.silva
 */
public interface GoogleOwnerController {
    /**
     * @return retorna o código
     */
    ResponseEntity<String> get();
}
