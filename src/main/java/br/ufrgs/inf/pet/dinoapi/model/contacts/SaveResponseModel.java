package br.ufrgs.inf.pet.dinoapi.model.contacts;

public class SaveResponseModel {

    Long version;

    ContactModel contactResponseModel;

    public SaveResponseModel() {}

    public SaveResponseModel(Long version, ContactModel model) {
        this.setVersion(version);
        this.setContactResponseModel(model);
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public ContactModel getContactResponseModel() {
        return contactResponseModel;
    }

    public void setContactResponseModel(ContactModel contactResponseModel) {
        this.contactResponseModel = contactResponseModel;
    }
}
