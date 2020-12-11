package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleAuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleGrantRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleRefreshAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.web_socket.WebSocketAuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthController {
    /**
     * Requisita um token de acesso Google pelo token de autenticação
     *
     * @param googleAuthRequestModel - Objeto do tipo {@link GoogleAuthRequestModel}
     * @return Objeto {@link GoogleAuthResponseModel} com o novo token do google ou mensagem de erro
     */
    ResponseEntity<?> googleAuthRequest(GoogleAuthRequestModel googleAuthRequestModel);

    /**
     * Requisita uma nova permissão para o Google
     *
     * @param googleGrantRequestModel - Objeto do tipo {@link GoogleGrantRequestModel}
     * @return Objeto {@link GoogleAuthResponseModel} com o novo token do google ou mensagem de erro
     */
    ResponseEntity<?> googleGrantRequest(GoogleGrantRequestModel googleGrantRequestModel);

    /**
     * Requisita um token de acesso para conexão com o WebSocket
     *
     * @return Objeto {@link WebSocketAuthResponse} com o token gerado
     */
    ResponseEntity<WebSocketAuthResponse> webSocketAuthRequest();

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
}
