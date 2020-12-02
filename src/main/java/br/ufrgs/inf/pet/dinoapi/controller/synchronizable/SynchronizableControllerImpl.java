package br.ufrgs.inf.pet.dinoapi.controller.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteAllModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableGetModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableSaveAllModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableDeleteAllResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableListResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableResponseModel;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * Base Controller with get, getAll, save/update and delete for synchronizable entity
 *
 * @param <ENTITY> synchronizable entity
 * @param <ID> type of synchronizable entity id
 * @param <DATA_MODEL> data model of synchronizable entity
 * @param <REPOSITORY> repository of synchronizable entity
 * @param <SERVICE> service of synchronizable entity
 */
public abstract class SynchronizableControllerImpl<
        ENTITY extends SynchronizableEntity<ID>,
        ID extends Comparable<ID>,
        DATA_MODEL extends SynchronizableDataModel<ENTITY, ID>,
        REPOSITORY extends CrudRepository<ENTITY, ID>,
        SERVICE extends SynchronizableServiceImpl<ENTITY, ID, DATA_MODEL, REPOSITORY>>
        implements SynchronizableController<ENTITY, ID, DATA_MODEL> {

    protected final SERVICE service;

    protected SynchronizableControllerImpl(SERVICE service) {
        this.service = service;
    }

    @Override
    @GetMapping("get/")
    public ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>> get(
            @Valid @RequestBody SynchronizableGetModel<ID> model) {
        return service.get(model);
    }

    @Override
    @PostMapping("save/")
    public ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>> save(
            @Valid @RequestBody DATA_MODEL model) {
        return service.save(model);
    }

    @Override
    @DeleteMapping("delete/")
    public ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>> delete(
            @Valid @RequestBody SynchronizableDeleteModel<ID> model) {
        return service.delete(model);
    }

    @Override
    @GetMapping("get/all/")
    public ResponseEntity<SynchronizableListResponseModel<ENTITY, ID, DATA_MODEL>> getAll() {
        return service.getAll();
    }

    @Override
    @PostMapping("save/all/")
    public ResponseEntity<SynchronizableListResponseModel<ENTITY, ID, DATA_MODEL>>
    saveAll(@Valid @RequestBody SynchronizableSaveAllModel<ENTITY, ID, DATA_MODEL> model) {
        return service.saveAll(model);
    }

    @Override
    @GetMapping("delete/all/")
    public ResponseEntity<SynchronizableDeleteAllResponseModel<ID>>
    deleteAll(@Valid @RequestBody SynchronizableDeleteAllModel<ID> model) {
        return service.deleteAll(model);
    }
}