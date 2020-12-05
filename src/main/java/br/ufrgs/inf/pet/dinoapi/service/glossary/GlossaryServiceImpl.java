package br.ufrgs.inf.pet.dinoapi.service.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.glossary.GlossaryItem;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryItemDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.glossary.GlossaryItemRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.topic.GenericTopicMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GlossaryServiceImpl extends SynchronizableServiceImpl<GlossaryItem, Long, GlossaryItemDataModel, GlossaryItemRepository> {
    @Autowired
    public GlossaryServiceImpl(GlossaryItemRepository glossaryItemRepository, AuthServiceImpl authService,
                               GenericTopicMessageServiceImpl genericTopicMessageService) {
        super(glossaryItemRepository, authService, genericTopicMessageService);
    }

    @Override
    public GlossaryItemDataModel convertEntityToModel(GlossaryItem entity) {
        final GlossaryItemDataModel model =  new GlossaryItemDataModel();
        model.setText(entity.getText());
        model.setSubtitle(entity.getSubtitle());
        model.setFullText(entity.getFullText());
        model.setTitle(entity.getTitle());

        return model;
    }

    @Override
    public GlossaryItem convertModelToEntity(GlossaryItemDataModel model) {
        final GlossaryItem glossaryItem = new GlossaryItem();
        glossaryItem.setTitle(model.getTitle());
        glossaryItem.setText(model.getText());
        glossaryItem.setSubtitle(model.getSubtitle());
        glossaryItem.setFullText(model.getFullText());

        return glossaryItem;
    }

    @Override
    public void updateEntity(GlossaryItem entity, GlossaryItemDataModel model) {
        entity.setFullText(model.getFullText());
        entity.setSubtitle(model.getSubtitle());
        entity.setText(model.getText());
        entity.setTitle(model.getTitle());
    }

    @Override
    public Optional<GlossaryItem> getEntityByIdAndUserId(Long id, User user) {
        return this.repository.findById(id);
    }

    @Override
    public List<GlossaryItem> getEntitiesByUserId(User user) {
        return this.repository.findAll();
    }

    @Override
    public List<GlossaryItem> getEntitiesByIdsAndUserId(List<Long> ids, User user) {
        return this.repository.findByIds(ids);
    }

    @Override
    public WebSocketDestinationsEnum getUpdateWebsocketDestination() {
        return WebSocketDestinationsEnum.GLOSSARY_UPDATE;
    }

    @Override
    public WebSocketDestinationsEnum getDeleteWebsocketDestination() {
        return WebSocketDestinationsEnum.GLOSSARY_DELETE;
    }
}
