package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import java.io.Serializable;

public class SynchronizableGenericListLocalIdModel
        <ID extends Comparable<ID> & Serializable, DATA_TYPE extends SynchronizableDataLocalIdModel<ID>>
        extends SynchronizableGenericListModel<ID, DATA_TYPE> {
}
