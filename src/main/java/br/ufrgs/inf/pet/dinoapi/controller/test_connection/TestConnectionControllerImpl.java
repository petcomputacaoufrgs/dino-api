package br.ufrgs.inf.pet.dinoapi.controller.test_connection;

import br.ufrgs.inf.pet.dinoapi.service.test_connection.TestConnectionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class TestConnectionControllerImpl implements TestConnectionController {

    private final TestConnectionServiceImpl testConnectionService;

    @Autowired
    public TestConnectionControllerImpl(TestConnectionServiceImpl testConnectionService) {
        this.testConnectionService = testConnectionService;
    }

    @Override
    @GetMapping("public/test_connection/")
    public ResponseEntity<String> get() {
        return testConnectionService.getTest();
    }

    @GetMapping("public/data/")
    public ResponseEntity<LocalDateTime> getData() {
        return new ResponseEntity<>(LocalDateTime.now(), HttpStatus.OK);
    }
}
