package br.ufrgs.inf.pet.dinoapi.websocket.service;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.model.SynchronizableWSDeleteModel;
import br.ufrgs.inf.pet.dinoapi.websocket.model.SynchronizableWSGenericModel;
import br.ufrgs.inf.pet.dinoapi.websocket.model.SynchronizableWSUpdateModel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.io.Serializable;
import java.util.List;

public abstract class SynchronizableMessageService<
        ID extends Comparable<ID> & Serializable,
        LOCAL_ID,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID, LOCAL_ID>> {

    protected static final String webSocketUpdateURL = "/update/";
    protected static final String webSocketDeleteURL = "/delete/";

    protected final SimpMessagingTemplate simpMessagingTemplate;

    protected final OAuthServiceImpl authService;


    public SynchronizableMessageService(SimpMessagingTemplate simpMessagingTemplate,
                                        OAuthServiceImpl authService) {
        this.authService = authService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendUpdateMessage(List<DATA_MODEL> data, WebSocketDestinationsEnum pathEnum, Auth auth) {
        if (!data.isEmpty()) {
            this.sendModel(this.generateUpdateModel(data), pathEnum.getValue() + webSocketUpdateURL, auth);
        }
    }

    public void sendUpdateMessage(List<DATA_MODEL> data, WebSocketDestinationsEnum pathEnum, User user) {
        if (!data.isEmpty()) {
            this.sendModel(this.generateUpdateModel(data), pathEnum.getValue() + webSocketUpdateURL, user);
        }
    }

    public void sendDeleteMessage(List<ID> data, WebSocketDestinationsEnum pathEnum, Auth auth) {
        if(!data.isEmpty()) {
            this.sendModel(this.generateDeleteModel(data), pathEnum.getValue() + webSocketDeleteURL, auth);
        }
    }

    public void sendDeleteMessage(List<ID> data, WebSocketDestinationsEnum pathEnum, User user) {
        if(!data.isEmpty()) {
            this.sendModel(this.generateDeleteModel(data), pathEnum.getValue() + webSocketDeleteURL, user);
        }
    }

    private SynchronizableWSGenericModel<DATA_MODEL> generateUpdateModel(List<DATA_MODEL> data) {
        final SynchronizableWSUpdateModel<ID, DATA_MODEL> updateModel = new SynchronizableWSUpdateModel<>();
        updateModel.setData(data);

        return updateModel;
    }

    private SynchronizableWSGenericModel<ID> generateDeleteModel(List<ID> data) {
        final SynchronizableWSDeleteModel<ID> deleteModel = new SynchronizableWSDeleteModel<>();
        deleteModel.setData(data);

        return deleteModel;
    }

    protected abstract <TYPE> void sendModel(SynchronizableWSGenericModel<TYPE> data, String url, Auth auth);

    protected abstract <TYPE> void sendModel(SynchronizableWSGenericModel<TYPE> data, String url, User user);
}
