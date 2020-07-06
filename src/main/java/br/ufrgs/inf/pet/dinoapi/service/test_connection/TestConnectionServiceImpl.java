package br.ufrgs.inf.pet.dinoapi.service.test_connection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TestConnectionServiceImpl implements TestConnectionService {

    @Override
    public ResponseEntity<String> getTest() {
        return new ResponseEntity<>("Conectado", HttpStatus.OK);
    }

}
