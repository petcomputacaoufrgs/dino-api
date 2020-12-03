package br.ufrgs.inf.pet.dinoapi.websocket.model.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

public class SynchronizableWSUpdateModel<
        ENTITY extends SynchronizableEntity<ID>,
        ID extends Comparable<ID>,
        DATA_MODEL extends SynchronizableDataModel<ENTITY, ID>
        > extends SynchronizableWSGenericModel<DATA_MODEL> { }
