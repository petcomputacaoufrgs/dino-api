package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.web_socket.WebSocketAuthResponse;
import br.ufrgs.inf.pet.dinoapi.configuration.security.DinoCredentials;
import br.ufrgs.inf.pet.dinoapi.configuration.security.DinoUser;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthService {

    Auth generateAuth(User user);

    ResponseEntity<WebSocketAuthResponse> webSocketAuthRequest();

    ResponseEntity<?> refreshAuth(AuthRefreshRequestModel authRefreshRequestModel);

    Auth findByAccessToken(String accessToken);

    Auth findByWebSocketToken(String webSocketToken);

    Boolean canConnectToWebSocket(Auth auth);

    Auth getCurrentAuth();

    User getCurrentUser();

    DinoUser getPrincipal();

    DinoCredentials getCredentials();

    Boolean isValidToken(Auth auth);

    Claims decodeAccessToken(String accessToken);

    List<String> getAllUserWebSocketTokenExceptCurrentByUser();

    List<String> getAllUserWebSocketTokenByUser(User user);

    void setWebSocketConnected();
}
