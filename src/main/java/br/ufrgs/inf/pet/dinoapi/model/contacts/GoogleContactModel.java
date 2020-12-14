package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.constants.GoogleContactConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GoogleContactModel extends SynchronizableDataModel<Long> {

    @NotNull(message = GoogleContactConstants.RESOURCE_NAME_NULL_MESSAGE)
    @Size(max = GoogleContactConstants.RESOURCE_NAME_MAX, message = GoogleContactConstants.RESOURCE_NAME_MAX_MESSAGE)
    private String resourceName;

    @NotNull(message = GoogleContactConstants.CONTACT_ID_NULL_MESSAGE)
    private Long contactId;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
}
