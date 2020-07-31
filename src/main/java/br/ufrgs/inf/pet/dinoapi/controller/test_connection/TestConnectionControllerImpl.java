package br.ufrgs.inf.pet.dinoapi.controller.test_connection;

import br.ufrgs.inf.pet.dinoapi.service.test_connection.TestConnectionServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.service.glossary.GlossaryWebSocketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test_connection/")
public class TestConnectionControllerImpl implements TestConnectionController{

    @Autowired
    TestConnectionServiceImpl testConnectionService;

    @Autowired
    GlossaryWebSocketServiceImpl glossaryWebSocketService;

    @Override
    @GetMapping
    public ResponseEntity<String> get() {
        return testConnectionService.getTest();
    }
}
