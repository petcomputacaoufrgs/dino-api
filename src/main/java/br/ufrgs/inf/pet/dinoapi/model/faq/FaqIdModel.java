package br.ufrgs.inf.pet.dinoapi.model.faq;

public class FaqIdModel {
    private Long id;

    public FaqIdModel() {} //NUNCA SE ESQUECER DESSA MERDA

    public FaqIdModel(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
