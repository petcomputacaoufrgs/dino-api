package br.ufrgs.inf.pet.dinoapi.controller.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableResponseModel;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public abstract class SynchronizableControllerImpl<
        ENTITY extends SynchronizableEntity,
        ID,
        DATA_MODEL extends SynchronizableDataModel,
        REPOSITORY extends CrudRepository<ENTITY, ID>,
        SERVICE extends SynchronizableService<ENTITY, ID, DATA_MODEL, REPOSITORY>> implements SynchronizableController<DATA_MODEL> {

    protected final SERVICE service;

    protected SynchronizableControllerImpl(SERVICE service) {
        this.service = service;
    }

    @GetMapping("get/")
    public ResponseEntity<SynchronizableResponseModel> get(@Valid @RequestBody SynchronizableDeleteModel model) {
        return service.get(model);
    }

    @PostMapping("save/")
    public ResponseEntity<SynchronizableResponseModel> save(@Valid @RequestBody SynchronizableRequestModel<DATA_MODEL> model) {
        return service.save(model);
    }

    @DeleteMapping("delete/")
    public ResponseEntity<SynchronizableResponseModel> delete(@Valid @RequestBody SynchronizableDeleteModel model) {
        return service.delete(model);
    }

}