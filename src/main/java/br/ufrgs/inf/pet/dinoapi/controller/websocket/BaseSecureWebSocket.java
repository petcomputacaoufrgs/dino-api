package br.ufrgs.inf.pet.dinoapi.controller.websocket;

import br.ufrgs.inf.pet.dinoapi.model.websocket.SubscribeResponse;

import java.security.Principal;

public interface BaseSecureWebSocket {
    SubscribeResponse subscribe(Principal user);
}
