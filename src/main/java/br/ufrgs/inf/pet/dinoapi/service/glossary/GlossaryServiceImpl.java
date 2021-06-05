package br.ufrgs.inf.pet.dinoapi.service.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.glossary.GlossaryItem;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryItemDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.glossary.GlossaryItemRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableTopicMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GlossaryServiceImpl extends SynchronizableServiceImpl<GlossaryItem, Long, GlossaryItemDataModel, GlossaryItemRepository> {
    @Autowired
    public GlossaryServiceImpl(GlossaryItemRepository glossaryItemRepository, AuthServiceImpl authService,
                               ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                               SynchronizableTopicMessageService<Long, GlossaryItemDataModel> synchronizableTopicMessageService) {
        super(glossaryItemRepository, authService, clockService, synchronizableTopicMessageService, logAPIErrorService);
    }

    @Override
    public List<PermissionEnum> getNecessaryPermissionsToEdit() {
        final List<PermissionEnum> authorities = new ArrayList<>();
        authorities.add(PermissionEnum.ADMIN);
        authorities.add(PermissionEnum.STAFF);
        return authorities;
    }

    @Override
    protected List<GlossaryItemDataModel> treatBeforeSaveAll(List<GlossaryItemDataModel> models) {
        models = this.removeRepeatedTitlesInModels(models);

        this.treatRepeatedTitles(models);

        return models;
    }

    @Override
    protected void treatBeforeSave(GlossaryItemDataModel model) {
        final Optional<GlossaryItem> entitySearch = this.repository.findByTitle(model.getTitle());
        entitySearch.ifPresent(entity -> {
            model.setId(entity.getId());
        });
    }

    @Override
    public GlossaryItemDataModel convertEntityToModel(GlossaryItem entity) {
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setText(entity.getText());
        model.setSubtitle(entity.getSubtitle());
        model.setFullText(entity.getFullText());
        model.directSetTitle(entity.getTitle());

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
    public Optional<GlossaryItem> findEntityByIdThatUserCanRead(Long id, Auth auth) {
        return this.repository.findById(id);
    }

    @Override
    public Optional<GlossaryItem> findEntityByIdThatUserCanEdit(Long id, Auth auth) {
        return this.repository.findById(id);
    }

    @Override
    public List<GlossaryItem> findEntitiesThatUserCanRead(Auth auth) {
        return this.repository.findAll();
    }

    @Override
    public List<GlossaryItem> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) {
        return this.repository.findByIds(ids);
    }

    @Override
    public List<GlossaryItem> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) {
        return this.repository.findAllExcludingIds(ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.GLOSSARY;
    }

    private List<GlossaryItemDataModel> removeRepeatedTitlesInModels(List<GlossaryItemDataModel> models) {
        final HashMap<String, GlossaryItemDataModel> titleHashMap = new HashMap<>();
        for(GlossaryItemDataModel model : models) {
            GlossaryItemDataModel existentItem = titleHashMap.get(model.getTitle());
            if (existentItem == null || existentItem.getLastUpdate().isBefore(model.getLastUpdate())) {
                titleHashMap.put(model.getTitle(), model);
            }
        }

        return new ArrayList<>(titleHashMap.values());
    }

    private void treatRepeatedTitles(List<GlossaryItemDataModel> models) {
        final List<String> titles = models.stream().map(GlossaryItemDataModel::getTitle).collect(Collectors.toList());
        final List<GlossaryItem> entities = this.repository.findAllByTitlesOrderedByTitle(titles);
        if (entities.size() > 0) {
            final List<GlossaryItemDataModel> orderedModels = models.stream().sorted(Comparator.comparing(GlossaryItemDataModel::getTitle)).collect(Collectors.toList());
            int count = 0;
            GlossaryItem entity;
            for (GlossaryItemDataModel model : orderedModels) {
                if (count >= entities.size()) break;
                entity = entities.get(count);
                if (model.getTitle().equals(entity.getTitle())) {
                    if (model.getId() == null) {
                        model.setId(entity.getId());
                    } else if (!entity.getId().equals(model.getId())) {
                        model.changeRepeatedTitle(model.getId());
                    }
                }
                count++;
            }
        }
    }
}
