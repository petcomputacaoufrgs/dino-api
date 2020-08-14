package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleAuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleRefreshAuthResponseModel;
import org.springframework.http.ResponseEntity;

public interface AuthController {
    /**
     * Requisita um token de acesso Google pelo token de autenticação
     *
     * @param token - Token de autenticação do Google
     * @return Objeto {@link GoogleAuthResponseModel} com o novo token do google ou mensagem de erro
     */
    ResponseEntity<?> googleAuthRequest(GoogleAuthRequestModel token);

    /**
     * Requisita a atualização do token de acesso
     *
     * @return Objeto {@link AuthResponseModel} com o novo token de acesso ou mensagem de erro
     */
    ResponseEntity<?> refreshAuth(AuthRefreshRequestModel authRefreshRequestModel);

    /**
     * Requisita a atualização do token de acesso Google
     *
     * @return Objeto {@link GoogleRefreshAuthResponseModel} com o novo token de acesso ou erro
     */
    ResponseEntity<?> googleRefreshAuth();

    /**
     * Limpa as informações da autenticação corrente
     *
     * @return Mensagem de remoção
     */
    ResponseEntity<?> logout();
}
