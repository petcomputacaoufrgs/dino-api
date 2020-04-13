package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.model.auth.GoogleAuthRequestModel;
import org.springframework.http.ResponseEntity;

/**
 * Controller para gerenciar a autenticação (Endpoint)
 *
 * @author joao.silva
 */

public interface AuthController {
    /**
     * Requisita um token de acesso Google pelo token de autenticação
     *
     * @param token - Token de autenticação do Google
     * @return token validado
     */
    ResponseEntity<?> googleAuthRequest(GoogleAuthRequestModel token);
}
