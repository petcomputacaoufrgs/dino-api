package br.ufrgs.inf.pet.dinoapi.controller.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;

public abstract class SynchronizableController<T extends SynchronizableEntity> {
    protected final SynchronizableService<T> service;

    protected SynchronizableController(SynchronizableService<T> service) {
        this.service = service;
    }

    @PutMapping("sync/")
    public ResponseEntity<?> updateOrder(SynchronizableModel<T> model) {
        return this.service.synchronize(model);
    }
}
