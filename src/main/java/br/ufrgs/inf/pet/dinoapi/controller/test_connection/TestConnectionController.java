package br.ufrgs.inf.pet.dinoapi.controller.test_connection;

import br.ufrgs.inf.pet.dinoapi.model.TestModel;
import org.springframework.http.ResponseEntity;

/**
 * Controller para testar a conexão com a API WebAPI
 * @author joao.silva
 */
public interface TestConnectionController {
    /**
     * @param entry - Texto de entrada para teste de retorno
     * @return texto de entrada
     */
    ResponseEntity<TestModel> get(String entry);

    /**
     * @param entry - Texto de entrada para teste de retorno
     * @return texto de entrada
     */
    ResponseEntity<TestModel> put(String entry);

    /**
     * @param entry - Texto de entrada para teste de retorno
     * @return texto de entrada
     */
    ResponseEntity<TestModel> post(String entry);
}
