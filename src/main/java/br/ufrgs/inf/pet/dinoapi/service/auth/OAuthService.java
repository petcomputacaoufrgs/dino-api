package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.web_socket.WebSocketAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.security.DinoCredentials;
import br.ufrgs.inf.pet.dinoapi.security.DinoUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface OAuthService {

    Auth generateAuth(User user) throws JsonProcessingException;

    ResponseEntity<WebSocketAuthResponseModel> webSocketAuthRequest();

    ResponseEntity<?> refreshAuth(AuthRefreshRequestModel authRefreshRequestModel);

    Auth findByAccessToken(String accessToken);

    Auth findByWebSocketToken(String webSocketToken);

    boolean canConnectToWebSocket(Auth auth);

    Auth getCurrentAuth();

    DinoUser getPrincipal();

    DinoCredentials getCredentials();

    boolean isValidAccessToken(String token);

    boolean isValidRefreshToken(String token);

    boolean isValidWebSocketToken(String token);

    Claims decodeAccessToken(String accessToken);

    List<String> getAllUserWebSocketTokenExceptByAuth(Auth auth);

    List<String> getAllUserWebSocketToken(User user);

    void setWebSocketConnected();
}
