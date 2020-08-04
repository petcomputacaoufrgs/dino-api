package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleAuthRequestModel;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface AuthController {
    /**
     * Requisita um token de acesso Google pelo token de autenticação
     *
     * @param token - Token de autenticação do Google
     * @return token validado
     */
    ResponseEntity<?> googleAuthRequest(GoogleAuthRequestModel token, HttpServletRequest request);

    /**
     * Requisita a atualização do token de acesso Google
     *
     * @return novo token de acesso
     */
    ResponseEntity<?> googleRefreshAuth();

    /**
     * Limpa as informações da autenticação corrente
     *
     * @return Mensagem de remoção com status OK
     */
    ResponseEntity<?> logout();
}
