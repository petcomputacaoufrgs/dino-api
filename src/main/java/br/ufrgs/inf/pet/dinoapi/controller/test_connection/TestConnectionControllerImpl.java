package br.ufrgs.inf.pet.dinoapi.controller.test_connection;

import br.ufrgs.inf.pet.dinoapi.model.test_connection.TestModel;
import br.ufrgs.inf.pet.dinoapi.service.test_connection.TestConnectionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test_connection/")
public class TestConnectionControllerImpl implements TestConnectionController{

    @Autowired
    TestConnectionServiceImpl testConnectionService;

    @Override
    @GetMapping
    public ResponseEntity<TestModel> get(@RequestParam("entry") String entry) {
        return testConnectionService.getTest(entry);
    }

    @Override
    @PutMapping
    public ResponseEntity<TestModel> put(@RequestParam("entry") String entry) {
        return testConnectionService.putTest(entry);
    }

    @Override
    @PostMapping
    public ResponseEntity<TestModel> post(@RequestParam("entry") String entry) {
        return testConnectionService.postTest(entry);
    }
}
