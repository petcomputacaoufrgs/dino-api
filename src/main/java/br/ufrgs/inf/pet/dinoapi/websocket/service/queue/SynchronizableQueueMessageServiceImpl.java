package br.ufrgs.inf.pet.dinoapi.websocket.service.queue;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableMessageService;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.List;

@Service
public class SynchronizableQueueMessageServiceImpl<
        ID extends Comparable<ID> & Serializable,
        LOCAL_ID,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID, LOCAL_ID>>
        extends SynchronizableMessageService<ID, LOCAL_ID, DATA_MODEL> {

    @Autowired
    public SynchronizableQueueMessageServiceImpl(SimpMessagingTemplate simpMessagingTemplate,
                                                 AuthServiceImpl authService, GsonBuilder gsonBuilder) {
        super(simpMessagingTemplate, authService, gsonBuilder);
    }

    @Override
    protected void sendModel(String json, String url, Auth auth) {
        final List<String> webSocketTokens = authService.getAllUserWebSocketTokenExceptByAuth(auth);
        this.send(json, url, webSocketTokens);
    }

    private void send(String json, String url, List<String> webSocketTokens) {
        final String dest = this.generateQueueDest(url);
        webSocketTokens.forEach(webSocketToken -> {
            this.simpMessagingTemplate.convertAndSendToUser(webSocketToken, dest, json);
        });
    }

    private String generateQueueDest(String url) {
        return "/queue/" + url;
    }

}
