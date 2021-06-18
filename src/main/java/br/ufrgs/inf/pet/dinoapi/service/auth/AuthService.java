package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.web_socket.WebSocketAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.security.DinoCredentials;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthService {

    Auth generateAuth(User user) throws JsonProcessingException;

    ResponseEntity<WebSocketAuthResponseModel> webSocketAuthRequest();

    ResponseEntity<?> refreshAuth(AuthRefreshRequestModel authRefreshRequestModel);

    Auth findByAccessToken(String accessToken);

    Auth findByWebSocketToken(String webSocketToken);

    boolean canConnectToWebSocket(Auth auth);

    Auth getCurrentAuth();

    DinoCredentials getCredentials();

    boolean isValidAccessToken(String token);

    String getCurrentPermission();

    Claims decodeAccessToken(String accessToken);

    List<String> getAllUserWebSocketTokenExceptByAuth(Auth auth);

    List<String> getAllWebSocketTokenExceptByAuth(Auth auth);

    List<String> getAllUserWebSocketToken(User user);

    List<String> getAllAdminsWebSocketToken(Auth auth);

    void setWebSocketConnected();
}
