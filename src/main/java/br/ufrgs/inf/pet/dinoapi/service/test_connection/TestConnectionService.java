package br.ufrgs.inf.pet.dinoapi.service.test_connection;


import br.ufrgs.inf.pet.dinoapi.model.TestModel;
import org.springframework.http.ResponseEntity;

/**
 * Service para testes de conexão simples
 *
 * @author joao.silva
 */
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
