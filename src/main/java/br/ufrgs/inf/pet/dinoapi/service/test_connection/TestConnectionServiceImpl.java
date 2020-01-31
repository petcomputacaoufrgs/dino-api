package br.ufrgs.inf.pet.dinoapi.service.test_connection;

import br.ufrgs.inf.pet.dinoapi.model.test_connection.TestModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Implementação de {@link TestConnectionService}
 *
 * @author joao.silva
 */
@Service
public class TestConnectionServiceImpl implements TestConnectionService {

    @Override
    public ResponseEntity<TestModel> getTest(String entry) {
        TestModel model = new TestModel(entry);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TestModel> putTest(String entry) {
        TestModel model = new TestModel(entry);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TestModel> postTest(String entry) {
        TestModel model = new TestModel(entry);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
}
