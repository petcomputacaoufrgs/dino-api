package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import java.util.ArrayList;
import java.util.List;

public class ContactSaveModel {

    private Long frontId;
    private Long id;
    private String name;
    private List<PhoneSaveModel> phones;
    private String description;
    private String color;

    public void setByContact(Contact contact) {
        this.setId(contact.getId());
        this.setName(contact.getName());
        this.setPhones(contact.getPhones());
        this.setDescription(contact.getDescription());
        this.setColor(contact.getColor());
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

    public void setPhones(List<Phone> phones) {

        List<PhoneSaveModel> response = new ArrayList<>();
        for (Phone phone : phones) {
            PhoneSaveModel responseItem = new PhoneSaveModel();
            responseItem.setNumber(phone.getNumber());
            responseItem.setType(phone.getType());
            response.add(responseItem);
        }

        this.phones = response;
    }

    public List<PhoneSaveModel> getPhones() {
        return phones;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }


    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}


