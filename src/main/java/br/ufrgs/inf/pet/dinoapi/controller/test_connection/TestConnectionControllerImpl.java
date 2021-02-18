package br.ufrgs.inf.pet.dinoapi.controller.test_connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestConnectionControllerImpl implements TestConnectionController {

    @Autowired
    public TestConnectionControllerImpl() { }

    @Override
    @GetMapping("public/test_connection/")
    public ResponseEntity<Void> get() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
