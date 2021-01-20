package br.ufrgs.inf.pet.dinoapi.websocket.service.queue;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.model.SynchronizableWSGenericModel;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.List;

@Service
public class SynchronizableQueueMessageService<
        ID extends Comparable<ID> & Serializable,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID>>
        extends SynchronizableMessageService<ID, DATA_MODEL> {

    @Autowired
    public SynchronizableQueueMessageService(SimpMessagingTemplate simpMessagingTemplate,
                                             OAuthServiceImpl authService) {
        super(simpMessagingTemplate, authService);
    }

    @Override
    protected <TYPE> void sendModel(SynchronizableWSGenericModel<TYPE> data, String url, Auth auth) {
        final List<String> webSocketTokens = authService.getAllUserWebSocketTokenExceptByAuth(auth);
        this.send(data, url, webSocketTokens);
    }


    @Override
    protected <TYPE> void sendModel(SynchronizableWSGenericModel<TYPE> data, String url, User user) {
        final List<String> webSocketTokens = authService.getAllUserWebSocketToken(user);
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
