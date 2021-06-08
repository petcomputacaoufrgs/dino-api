package br.ufrgs.inf.pet.dinoapi.controller.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.*;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

import static br.ufrgs.inf.pet.dinoapi.constants.PathConstants.*;

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
        ID extends Comparable<ID> & Serializable,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID>,
        REPOSITORY extends CrudRepository<ENTITY, ID>,
        SERVICE extends SynchronizableServiceImpl<ENTITY, ID, DATA_MODEL, REPOSITORY>>
        implements SynchronizableController<ID, DATA_MODEL> {

    protected final SERVICE service;

    protected SynchronizableControllerImpl(SERVICE service) {
        this.service = service;
    }

    @Override
    @GetMapping(GET)
    public ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>> get(
            @Valid @RequestBody SynchronizableGetModel<ID> model) {
        return service.get(model);
    }

    @Override
    @PostMapping(SAVE)
    public ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>> save(
            @Valid @RequestBody DATA_MODEL model) {
        return service.save(model);
    }

    @Override
    @DeleteMapping(DELETE)
    public ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>> delete(
            @Valid @RequestBody SynchronizableDeleteModel<ID> model) {
        return service.delete(model);
    }

    @Override
    @GetMapping(GET_ALL)
    public ResponseEntity<SynchronizableListDataResponseModelImpl<ID, DATA_MODEL>> getAll() {
        return service.getAll();
    }

    @Override
    @PostMapping(SAVE_ALL)
    public ResponseEntity<SynchronizableSaveAllResponseModel<ID, DATA_MODEL>>
    saveAll(@Valid @RequestBody SynchronizableSaveAllModel<ID, DATA_MODEL> model) {
        return service.saveAll(model);
    }

    @Override
    @DeleteMapping(DELETE_ALL)
    public  ResponseEntity<SynchronizableGenericDataResponseModelImpl<List<ID>>>
    deleteAll(@Valid @RequestBody SynchronizableDeleteAllListModel<ID> model) {
        return service.deleteAll(model);
    }

    @Override
    @PutMapping(SYNC)
    public ResponseEntity<SynchronizableSyncResponseModel<ID, DATA_MODEL>>
    sync(@Valid @RequestBody SynchronizableSaveSyncModel<ID, DATA_MODEL> model){
        return service.sync(model);
    }
}