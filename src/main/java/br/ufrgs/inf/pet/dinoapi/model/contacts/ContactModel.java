package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import java.util.List;
import java.util.stream.Collectors;

public class ContactModel {
    private Long id;
    private String name;
    private List<PhoneModel> phones;
    private String description;
    private Byte color;

    public ContactModel(){}

    public ContactModel(Contact contact) {
        this.setId(contact.getId());
        this.setName(contact.getName());
        this.setPhones(contact.getPhones().stream().map(PhoneModel::new).collect(Collectors.toList()));
        this.setDescription(contact.getDescription());
        this.setColor(contact.getColor());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPhones(List<PhoneModel> phones) {
        this.phones = phones;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(Byte color) {
        this.color = color;
    }

    public Long getId() { return id; }


    public String getDescription() {
        return description;
    }

    public List<PhoneModel> getPhones() { return phones; }

    public String getName() { return name; }

    public Byte getColor() { return color; }

}


