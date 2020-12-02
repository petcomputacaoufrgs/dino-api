package br.ufrgs.inf.pet.dinoapi.websocket.service.queue.generic;

import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.utils.JsonUtils;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenericQueueMessageServiceImpl implements GenericQueueMessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final AuthServiceImpl authService;

    @Autowired
    public GenericQueueMessageServiceImpl(SimpMessagingTemplate simpMessagingTemplate, AuthServiceImpl authService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.authService = authService;
    }

    @Override
    public void sendObjectMessage(Object object, WebSocketDestinationsEnum pathEnum) throws JsonProcessingException {
        final String message = this.getMessage(object);

        final List<String> webSocketTokens = authService.getAllUserWebSocketTokenExceptCurrentByUser();
        webSocketTokens.forEach(webSocketToken -> {
            this.simpMessagingTemplate.convertAndSendToUser(webSocketToken, pathEnum.getValue(), message);
        });
    }

    private String getMessage(Object object) throws JsonProcessingException {
        String message = "";
        if (object != null) {
            message = JsonUtils.convertObjectToJSON(object);
        }

        return message;
    }
}
