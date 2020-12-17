package br.ufrgs.inf.pet.dinoapi.websocket.service.topic.synchronizable;

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

@Service
public class SynchronizableTopicMessageServiceImpl<
        ID extends Comparable<ID> & Serializable,
        LOCAL_ID,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID, LOCAL_ID>> extends SynchronizableMessageService<ID, LOCAL_ID, DATA_MODEL> {

    @Autowired
    public SynchronizableTopicMessageServiceImpl(SimpMessagingTemplate simpMessagingTemplate,
                                                 AuthServiceImpl authService, GsonBuilder gsonBuilder) {
        super(simpMessagingTemplate, authService, gsonBuilder);
    }

    @Override
    protected void sendModel(String json, WebSocketDestinationsEnum pathEnum) {
        this.simpMessagingTemplate.convertAndSend(pathEnum.getValue(), json);
    }

    @Override
    protected void sendModel(String json, WebSocketDestinationsEnum pathEnum, User user) {
        this.simpMessagingTemplate.convertAndSend(pathEnum.getValue(), json);
    }
}
