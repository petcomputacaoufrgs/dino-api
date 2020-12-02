package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

import java.util.List;

/**
 * Response model for delete a list of synchronizable entity
 *
 * @param <ID> Id type of synchronizable
 */
public class SynchronizableDeleteAllResponseModel<ID extends Comparable<ID>>
        extends SynchronizableGenericResponseModel<List<ID>> {
}
