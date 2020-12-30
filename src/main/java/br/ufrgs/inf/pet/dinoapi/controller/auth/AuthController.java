package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleAuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleGrantRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.refresh_auth.GoogleRefreshAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.web_socket.WebSocketAuthResponseModel;
import org.springframework.http.ResponseEntity;

public interface AuthController {
    /**
     * Request google authentication
     *
     * @param googleAuthRequestDataModel - Object {@link GoogleAuthRequestModel}
     * @return Object {@link GoogleAuthResponseModel} with authentication or error
     */
    ResponseEntity<GoogleAuthResponseModel> googleAuthRequest(GoogleAuthRequestModel googleAuthRequestDataModel);

    /**
     * Request a Google Grant
     *
     * @param googleGrantRequestModel - Objeto do tipo {@link GoogleGrantRequestModel}
     * @return Object {@link GoogleRefreshAuthResponseModel} with new authentication or error
     */
    ResponseEntity<GoogleAuthResponseModel> googleGrantRequest(GoogleGrantRequestModel googleGrantRequestModel);

    /**
     * Update Google Access Token
     *
     * @return Object {@link GoogleRefreshAuthResponseModel} with new authentication or error
     */
    ResponseEntity<GoogleRefreshAuthResponseModel> googleRefreshAuth();

    /**
     * Request websocket authentication
     *
     * @return Object {@link WebSocketAuthResponseModel} with authentication
     */
    ResponseEntity<WebSocketAuthResponseModel> webSocketAuthRequest();

    /**
     * Request access token update
     *
     * @return Object {@link AuthRefreshResponseModel} with new authentication or error
     */
    ResponseEntity<AuthRefreshResponseModel> refreshAuth(AuthRefreshRequestModel authRefreshRequestModel);
}
