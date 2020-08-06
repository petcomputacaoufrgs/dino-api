package br.ufrgs.inf.pet.dinoapi.model.contacts;

public class SaveResponseModel {

    Long version;

    ContactModel responseModel;

    public SaveResponseModel() {}

    public SaveResponseModel(Long version, ContactModel model) {
        this.setVersion(version);
        this.setResponseModel(model);
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public ContactModel getResponseModel() {
        return responseModel;
    }

    public void setResponseModel(ContactModel responseModel) {
        this.responseModel = responseModel;
    }
}
