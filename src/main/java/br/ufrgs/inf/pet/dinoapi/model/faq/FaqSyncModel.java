package br.ufrgs.inf.pet.dinoapi.model.faq;

public class FaqSyncModel {

    private Long id;
    private Long version;

    public FaqSyncModel() {}

    public FaqSyncModel(Long id, Long version) {
        this.id = id;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
