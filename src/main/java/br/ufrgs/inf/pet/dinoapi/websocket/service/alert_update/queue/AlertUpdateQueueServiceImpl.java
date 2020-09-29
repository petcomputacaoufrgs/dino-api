package br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.queue;

import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.utils.JsonUtils;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.AlertUpdateModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertUpdateQueueServiceImpl implements AlertUpdateQueueService {

    protected final SimpMessagingTemplate simpMessagingTemplate;

    private final AuthServiceImpl authService;

    @Autowired
    public AlertUpdateQueueServiceImpl(SimpMessagingTemplate simpMessagingTemplate, AuthServiceImpl authService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.authService = authService;
    }

    @Override
    public void sendUpdateMessage(Long newVersion, WebSocketDestinationsEnum pathEnum) {
        final AlertUpdateModel model = new AlertUpdateModel();
        model.setNewVersion(newVersion);

        final List<String> webSocketTokens = authService.getAllUserWebSocketTokenExceptCurrentByUser();
        webSocketTokens.forEach(webSocketToken -> {
            this.simpMessagingTemplate.convertAndSendToUser(webSocketToken, pathEnum.getValue(), model);
        });
    }

    @Override
    public void sendUpdateIdMessage(Long newId, WebSocketDestinationsEnum pathEnum) {
        final AlertUpdateModel model = new AlertUpdateModel();
        model.setNewId(newId);

        final List<String> webSocketTokens = authService.getAllUserWebSocketTokenExceptCurrentByUser();
        webSocketTokens.forEach(webSocketToken -> {
            this.simpMessagingTemplate.convertAndSendToUser(webSocketToken, pathEnum.getValue(), model);
        });
    }

    @Override
    public void sendUpdateObjectMessage(Object object, WebSocketDestinationsEnum pathEnum) throws JsonProcessingException {
        final String message = JsonUtils.convertObjectToJSON(object);
        final List<String> webSocketTokens = authService.getAllUserWebSocketTokenExceptCurrentByUser();
        webSocketTokens.forEach(webSocketToken -> {
            this.simpMessagingTemplate.convertAndSendToUser(webSocketToken, pathEnum.getValue(), message);
        });
    }

}
