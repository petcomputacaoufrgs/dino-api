package br.ufrgs.inf.pet.dinoapi.model.faq;

import java.util.List;

public class FaqAllModel {

    private Long version;
    private List<FaqModel> faqs;
    public FaqAllModel(){}

    public FaqAllModel(Long version, List<FaqModel> faqs){
        this.setVersion(version);
        this.setFaqs(faqs);
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<FaqModel> getFaqs() {
        return faqs;
    }

    public void setFaqs(List<FaqModel> faqs) {
        this.faqs = faqs;
    }
}
