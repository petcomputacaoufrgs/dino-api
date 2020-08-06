package br.ufrgs.inf.pet.dinoapi.websocket.service.user;

import br.ufrgs.inf.pet.dinoapi.entity.User;

public interface UserWebSocketService {
    void sendUpdateMessage(User user);
}
