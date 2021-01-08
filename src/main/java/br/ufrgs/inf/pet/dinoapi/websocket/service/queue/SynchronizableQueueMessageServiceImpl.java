package br.ufrgs.inf.pet.dinoapi.websocket.service.queue;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.model.SynchronizableWSGenericModel;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableMessageService;
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
                                                 AuthServiceImpl authService) {
        super(simpMessagingTemplate, authService);
    }

    @Override
    protected <TYPE> void sendModel(SynchronizableWSGenericModel<TYPE> data, String url, Auth auth) {
        final List<String> webSocketTokens = authService.getAllUserWebSocketTokenExceptByAuth(auth);
        this.send(data, url, webSocketTokens);
    }

    private <TYPE> void send(SynchronizableWSGenericModel<TYPE> data, String url, List<String> webSocketTokens) {
        final String dest = this.generateQueueDest(url);
        for (String webSocketToken : webSocketTokens) {
            this.simpMessagingTemplate.convertAndSendToUser(webSocketToken, dest, data);
        }
    }

    private String generateQueueDest(String url) {
        return "/queue/" + url;
    }
}
