package br.ufrgs.inf.pet.dinoapi.controller.synchronizable;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableResponseModel;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

public interface SynchronizableController<DATA_MODEL extends SynchronizableDataModel> {
    ResponseEntity<SynchronizableResponseModel> get(SynchronizableDeleteModel model);

    ResponseEntity<SynchronizableResponseModel> save(SynchronizableRequestModel<DATA_MODEL> model);

    ResponseEntity<SynchronizableResponseModel> delete(SynchronizableDeleteModel model);
}
