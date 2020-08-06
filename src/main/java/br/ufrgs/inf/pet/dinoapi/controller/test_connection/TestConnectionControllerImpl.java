package br.ufrgs.inf.pet.dinoapi.controller.test_connection;

import br.ufrgs.inf.pet.dinoapi.service.test_connection.TestConnectionServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.service.glossary.GlossaryWebSocketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestConnectionControllerImpl implements TestConnectionController{

    private final TestConnectionServiceImpl testConnectionService;

    private final GlossaryWebSocketServiceImpl glossaryWebSocketService;

    @Autowired
    public TestConnectionControllerImpl(TestConnectionServiceImpl testConnectionService, GlossaryWebSocketServiceImpl glossaryWebSocketService) {
        this.testConnectionService = testConnectionService;
        this.glossaryWebSocketService = glossaryWebSocketService;
    }

    @Override
    @GetMapping("public/test_connection/")
    public ResponseEntity<String> get() {
        return testConnectionService.getTest();
    }
}
