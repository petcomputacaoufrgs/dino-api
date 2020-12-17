package br.ufrgs.inf.pet.dinoapi.websocket.service;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exclude_strategy.SynchronizableWSExcludeStrategy;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.model.synchronizable.SynchronizableWSDeleteModel;
import br.ufrgs.inf.pet.dinoapi.websocket.model.synchronizable.SynchronizableWSUpdateModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.io.Serializable;
import java.util.List;

public abstract class SynchronizableMessageService<
        ID extends Comparable<ID> & Serializable,
        LOCAL_ID,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID, LOCAL_ID>> {

    protected final SimpMessagingTemplate simpMessagingTemplate;

    protected final AuthServiceImpl authService;

    protected final GsonBuilder gsonBuilder;

    public SynchronizableMessageService(SimpMessagingTemplate simpMessagingTemplate,
                                        AuthServiceImpl authService, GsonBuilder gsonBuilder) {
        this.authService = authService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.gsonBuilder = gsonBuilder;
    }

    public void sendUpdateMessage(List<DATA_MODEL> data, WebSocketDestinationsEnum pathEnum) {
        if (!data.isEmpty()) {
            this.sendModel(this.generateUpdateModelJson(data), pathEnum);
        }
    }

    public void sendUpdateMessage(List<DATA_MODEL> data, WebSocketDestinationsEnum pathEnum, User user) {
        if (!data.isEmpty()) {
            this.sendModel(this.generateUpdateModelJson(data), pathEnum, user);
        }
    }

    public void sendDeleteMessage(List<ID> data, WebSocketDestinationsEnum pathEnum) {
        if(!data.isEmpty()) {
            this.sendModel(this.generateDeleteModelJson(data), pathEnum);
        }
    }

    private String generateUpdateModelJson(List<DATA_MODEL> data) {
        final SynchronizableWSUpdateModel<ID, DATA_MODEL> updateModel = new SynchronizableWSUpdateModel<>();
        updateModel.setData(data);

        return this.convertUpdateModelToJson(updateModel);
    }

    private String generateDeleteModelJson(List<ID> data) {
        final SynchronizableWSDeleteModel<ID> deleteModel = new SynchronizableWSDeleteModel<>();
        deleteModel.setData(data);

        return this.convertDeleteModelToJson(deleteModel);
    }

    protected String convertUpdateModelToJson(SynchronizableWSUpdateModel<ID, DATA_MODEL> updateModel) {
        final Gson gson = gsonBuilder.setExclusionStrategies(new SynchronizableWSExcludeStrategy()).create();

        return gson.toJson(updateModel);
    }

    protected String convertDeleteModelToJson(SynchronizableWSDeleteModel<ID> deleteModel) {
        final Gson gson = gsonBuilder.create();

        return gson.toJson(deleteModel);
    }

    protected abstract void sendModel(String json, WebSocketDestinationsEnum pathEnum);

    protected abstract void sendModel(String json, WebSocketDestinationsEnum pathEnum, User user);
}
