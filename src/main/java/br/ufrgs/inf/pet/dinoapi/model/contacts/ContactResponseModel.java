package br.ufrgs.inf.pet.dinoapi.model.contacts;
import java.util.ArrayList;
import java.util.List;

public class ContactResponseModel {

    Long version;

    List<ContactModel> responseModels;

    public ContactResponseModel() {
        this.setResponseModels(new ArrayList<>());
    }

    public ContactResponseModel(Long version, List<ContactModel> models) {
        this.setVersion(version);
        this.setResponseModels(models);
    }

        public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<ContactModel> getResponseModels() {
        return responseModels;
    }

    public void setResponseModels(List<ContactModel> responseModels) {
        this.responseModels = responseModels;
    }
}
