package br.ufrgs.inf.pet.dinoapi.websocket.interceptor;

import br.ufrgs.inf.pet.dinoapi.configuration.security.DinoAuthenticationToken;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import java.security.Principal;
import java.util.Map;

public class WebSocketUserInterceptor extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        final DinoAuthenticationToken dinoAuthenticationToken = (DinoAuthenticationToken)request.getPrincipal();
        final WebSocketAuthenticationToken webSocketUser = new WebSocketAuthenticationToken(dinoAuthenticationToken);
        return webSocketUser;
    }
}
