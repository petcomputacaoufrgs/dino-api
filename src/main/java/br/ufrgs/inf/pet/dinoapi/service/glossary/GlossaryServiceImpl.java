package br.ufrgs.inf.pet.dinoapi.service.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.glossary.GlossaryItem;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryItemDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.glossary.GlossaryItemRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.topic.SynchronizableTopicMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GlossaryServiceImpl extends SynchronizableServiceImpl<GlossaryItem, Long, Integer, GlossaryItemDataModel, GlossaryItemRepository> {
    @Autowired
    public GlossaryServiceImpl(GlossaryItemRepository glossaryItemRepository, OAuthServiceImpl authService,
                               ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                               SynchronizableTopicMessageServiceImpl<Long, Integer, GlossaryItemDataModel> synchronizableTopicMessageService) {
        super(glossaryItemRepository, authService, clockService, synchronizableTopicMessageService, logAPIErrorService);
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
    public GlossaryItem convertModelToEntity(GlossaryItemDataModel model, Auth auth) {
        final GlossaryItem glossaryItem = new GlossaryItem();
        glossaryItem.setTitle(model.getTitle());
        glossaryItem.setText(model.getText());
        glossaryItem.setSubtitle(model.getSubtitle());
        glossaryItem.setFullText(model.getFullText());

        return glossaryItem;
    }

    @Override
    public void updateEntity(GlossaryItem entity, GlossaryItemDataModel model, Auth auth) {
        entity.setFullText(model.getFullText());
        entity.setSubtitle(model.getSubtitle());
        entity.setText(model.getText());
        entity.setTitle(model.getTitle());
    }

    @Override
    public Optional<GlossaryItem> getEntityByIdAndUserAuth(Long id, Auth auth) {
        return this.repository.findById(id);
    }

    @Override
    public List<GlossaryItem> getEntitiesThatUserCanRead(Auth auth) {
        return this.repository.findAll();
    }

    @Override
    public List<GlossaryItem> getEntitiesByIdsAndUserAuth(List<Long> ids, Auth auth) {
        return this.repository.findByIds(ids);
    }

    @Override
    public List<GlossaryItem> getEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) {
        return this.repository.findAllExceptIds(ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.GLOSSARY;
    }
}
