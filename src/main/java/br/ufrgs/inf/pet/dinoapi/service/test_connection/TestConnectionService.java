package br.ufrgs.inf.pet.dinoapi.service.test_connection;


import br.ufrgs.inf.pet.dinoapi.model.test_connection.TestModel;
import org.springframework.http.ResponseEntity;

public interface TestConnectionService {
    /**
     * @param entry - Texto de entrada para teste de retorno
     * @return texto de entrada
     */
    ResponseEntity<TestModel> getTest(String entry);

    /**
     * @param entry - Texto de entrada para teste de retorno
     * @return texto de entrada
     */
    ResponseEntity<TestModel> putTest(String entry);

    /**
     * @param entry - Texto de entrada para teste de retorno
     * @return texto de entrada
     */
    ResponseEntity<TestModel> postTest(String entry);
}
