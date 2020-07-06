package br.ufrgs.inf.pet.dinoapi.service.test_connection;

import org.springframework.http.ResponseEntity;

public interface TestConnectionService {

    ResponseEntity<String> getTest();

}
