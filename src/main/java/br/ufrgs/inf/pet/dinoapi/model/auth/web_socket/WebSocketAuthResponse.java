package br.ufrgs.inf.pet.dinoapi.model.auth.web_socket;

import br.ufrgs.inf.pet.dinoapi.entity.Auth;

public class WebSocketAuthResponse {
    private String webSocketToken;

    public WebSocketAuthResponse(Auth auth) {
        this.webSocketToken = auth.getWebSocketToken();
    }


    public String getWebSocketToken() {
        return webSocketToken;
    }

    public void setWebSocketToken(String webSocketToken) {
        this.webSocketToken = webSocketToken;
    }
}
