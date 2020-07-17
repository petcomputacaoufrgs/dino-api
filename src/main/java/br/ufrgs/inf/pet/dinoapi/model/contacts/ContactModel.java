package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import java.util.ArrayList;
import java.util.List;

public class ContactModel {
    private Long id;
    private Long frontId;
    private String name;
    private List<PhoneModel> phones;
    private String description;
    private String color;

    public ContactModel(){}

    public ContactModel(Contact contact) {
        this.setId(contact.getId());
        this.setFrontId(contact.getFrontId());
        this.setName(contact.getName());
        this.setByPhones(contact.getPhones());
        this.setDescription(contact.getDescription());
        this.setColor(contact.getColor());
    }

    public void setByPhones(List<Phone> phones) {

        List<PhoneModel> response = new ArrayList<>();
        for (Phone phone : phones) {
            PhoneModel responseItem = new PhoneModel(phone);
            response.add(responseItem);
        }
        this.phones = response;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPhones(List<PhoneModel> phones) {
        this.phones = phones;
    }

    public void setFrontId(Long frontId) {
        this.frontId = frontId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getId() { return id; }

    public Long getFrontId() { return frontId; }

    public String getDescription() {
        return description;
    }

    public List<PhoneModel> getPhones() { return phones; }

    public String getName() { return name; }

    public String getColor() { return color; }

}


