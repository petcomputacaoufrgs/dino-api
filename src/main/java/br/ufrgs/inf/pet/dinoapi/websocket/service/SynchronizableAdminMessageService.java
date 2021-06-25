package br.ufrgs.inf.pet.dinoapi.websocket.service;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.model.SynchronizableWSGenericModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class SynchronizableAdminMessageService<
        ID extends Comparable<ID> & Serializable,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID>>
        extends SynchronizableMessageService<ID, DATA_MODEL> {

    @Autowired
    public SynchronizableAdminMessageService(SimpMessagingTemplate simpMessagingTemplate,
                                             AuthServiceImpl authService) {
        super(simpMessagingTemplate, authService);
    }

    @Override
    protected <TYPE> void sendModel(SynchronizableWSGenericModel<TYPE> data, String url, Auth auth) {
        final List<String> webSocketTokens = authService.getAllAdminsWebSocketToken(auth);
        this.send(data, url, webSocketTokens);
    }
}
