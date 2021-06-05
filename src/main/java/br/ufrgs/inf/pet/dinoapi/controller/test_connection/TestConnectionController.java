package br.ufrgs.inf.pet.dinoapi.controller.test_connection;

import org.springframework.http.ResponseEntity;

public interface TestConnectionController {

    /**
     * Test API connection
     */
    ResponseEntity<Void> get();

}
