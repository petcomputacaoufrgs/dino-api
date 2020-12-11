package br.ufrgs.inf.pet.dinoapi.websocket.service.queue;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.GenericMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenericQueueMessageServiceImpl extends GenericMessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final AuthServiceImpl authService;

    @Autowired
    public GenericQueueMessageServiceImpl(SimpMessagingTemplate simpMessagingTemplate, AuthServiceImpl authService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.authService = authService;
    }

    @Override
    public void sendObjectMessage(Object object, WebSocketDestinationsEnum pathEnum) {
        final List<String> webSocketTokens = authService.getAllUserWebSocketTokenExceptCurrentByUser();
        this.send(object, pathEnum, webSocketTokens);
    }

    @Override
    public void sendObjectMessage(Object object, WebSocketDestinationsEnum pathEnum, User user) {
        final List<String> webSocketTokens = authService.getAllUserWebSocketTokenByUser(user);
        this.send(object, pathEnum, webSocketTokens);
    }

    private void send(Object object, WebSocketDestinationsEnum pathEnum, List<String> webSocketTokens) {
        webSocketTokens.forEach(webSocketToken -> {
            this.simpMessagingTemplate.convertAndSendToUser(webSocketToken, pathEnum.getValue(), object);
        });
    }
}
