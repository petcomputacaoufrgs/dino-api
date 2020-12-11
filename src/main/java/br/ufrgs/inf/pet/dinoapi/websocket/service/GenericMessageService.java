package br.ufrgs.inf.pet.dinoapi.websocket.service;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;

public abstract class GenericMessageService {
    public abstract void sendObjectMessage(Object object, WebSocketDestinationsEnum pathEnum);
    public abstract void sendObjectMessage(Object object, WebSocketDestinationsEnum pathEnum, User user);
}
