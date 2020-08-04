package br.ufrgs.inf.pet.dinoapi.controller.test_connection;

import org.springframework.http.ResponseEntity;

public interface TestConnectionController {

    /**
     * Função para testar a conexão com a API
     *
     * @return Mensagem de sucesso de conexão
     */
    ResponseEntity<String> get();

}
