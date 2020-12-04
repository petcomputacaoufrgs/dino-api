package br.ufrgs.inf.pet.dinoapi.websocket.model.synchronizable;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

import java.io.Serializable;

public class SynchronizableWSUpdateModel<
        ID extends Comparable<ID> & Serializable,
        DATA_MODEL extends SynchronizableDataModel<ID>
        > extends SynchronizableWSGenericModel<DATA_MODEL> { }
