package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import java.util.ArrayList;
import java.util.List;

public class ContactModel {
    private Long id;
    private String name;
    private List<PhoneModel> phones;
    private String description;
    private String color;

    public void setByContact(Contact contact) {
        this.setId(contact.getId());
        this.setName(contact.getName());
        this.setByPhones(contact.getPhones());
        this.setDescription(contact.getDescription());
        this.setColor(contact.getDescription());
    }



    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setByPhones(List<Phone> phones) {

        List<PhoneModel> response = new ArrayList<>();
        for (Phone phone : phones) {
            PhoneModel responseItem = new PhoneModel(phone);
            response.add(responseItem);
        }
        this.phones = response;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public List<PhoneModel> getPhones() { return phones; }

    public String getName() { return name; }

    public String getColor() { return color; }

}


