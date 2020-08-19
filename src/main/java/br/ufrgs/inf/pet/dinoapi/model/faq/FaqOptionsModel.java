package br.ufrgs.inf.pet.dinoapi.model.faq;

import java.util.List;

public class FaqOptionsModel {

    private Long version;
    private List<FaqOptionModel> options;

    public FaqOptionsModel(){}

    public FaqOptionsModel(Long version, List<FaqOptionModel> options) {
        this.version = version;
        this.options = options;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<FaqOptionModel> getOptions() {
        return options;
    }

    public void setOptions(List<FaqOptionModel> options) {
        this.options = options;
    }
}
