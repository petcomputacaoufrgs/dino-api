package br.ufrgs.inf.pet.dinoapi.model.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;

import java.util.List;
import java.util.stream.Collectors;

public class FaqModel {
    private Long id;
    private Long version;
    private String title;
    private List<FaqItemModel> items;
    public FaqModel(){}

    public FaqModel(Faq faq){
        this.setId(faq.getId());
        this.setVersion(faq.getVersion());
        this.setTitle(faq.getTitle());
        this.setItems(faq.getItems().stream().map(FaqItemModel::new).collect(Collectors.toList()));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FaqItemModel> getItems() {
        return items;
    }

    public void setItems(List<FaqItemModel> items) {
        this.items = items;
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
