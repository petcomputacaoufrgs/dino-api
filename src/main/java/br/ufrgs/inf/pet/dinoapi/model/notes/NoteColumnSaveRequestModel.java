package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NoteColumnSaveRequestModel {
    private Long id;

    @NotNull
    private Integer order;

    @NotNull
    @Size(max = 50, message = "title should not be more than 50.")
    private String title;

    @NotNull
    private Long lastUpdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
