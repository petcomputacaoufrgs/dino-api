package br.ufrgs.inf.pet.dinoapi.model.contacts;
import java.util.ArrayList;
import java.util.List;

public class SaveResponseModelAll {

    Long version;

    List<ContactModel> contactResponseModels;

    public SaveResponseModelAll() {
        this.setContactResponseModels(new ArrayList<>());
    }

    public SaveResponseModelAll(Long version, List<ContactModel> models) {
        this.setVersion(version);
        this.setContactResponseModels(models);
    }

        public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<ContactModel> getContactResponseModels() {
        return contactResponseModels;
    }

    public void setContactResponseModels(List<ContactModel> contactResponseModels) {
        this.contactResponseModels = contactResponseModels;
    }
}
