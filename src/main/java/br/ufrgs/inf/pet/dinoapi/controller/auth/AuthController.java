package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleAuthRequestModel;
import org.springframework.http.ResponseEntity;

public interface AuthController {
    /**
     * Requisita um token de acesso Google pelo token de autenticação
     *
     * @param token - Token de autenticação do Google
     * @return token validado
     */
    ResponseEntity<?> googleAuthRequest(GoogleAuthRequestModel token);

    /**
     * Limpa as informações da autenticação corrente
     *
     * @return Mensagem de remoção com status OK
     */
    ResponseEntity<?> logout();
}
