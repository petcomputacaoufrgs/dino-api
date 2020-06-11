package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import java.util.ArrayList;

public class ContactResponseModel {
    private Long id;
    private String name;
    private ArrayList<Phone> phones;
    private String description;

    public void setByContact(Contact contact) {
        this.setId(contact.getId());
        this.setName(contact.getName());
        this.setPhones(contact.getPhones());
        this.setDescription(contact.getDescription());
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

    public void setPhones(ArrayList<Phone> phones) {
        this.phones = phones;
    }
}


