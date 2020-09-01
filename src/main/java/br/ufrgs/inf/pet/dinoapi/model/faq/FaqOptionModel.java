package br.ufrgs.inf.pet.dinoapi.model.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;

public class FaqOptionModel {

    private Long id;
    private String title;

    public FaqOptionModel(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public FaqOptionModel(Faq faq) {
        this.id = faq.getId();
        this.title = faq.getTitle();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
