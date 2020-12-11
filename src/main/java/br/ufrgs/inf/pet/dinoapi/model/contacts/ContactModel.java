package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

public class ContactModel extends SynchronizableDataModel<Long> {

    private String name;

    private String description;

    private Byte color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getColor() {
        return color;
    }

    public void setColor(Byte color) {
        this.color = color;
    }
}
