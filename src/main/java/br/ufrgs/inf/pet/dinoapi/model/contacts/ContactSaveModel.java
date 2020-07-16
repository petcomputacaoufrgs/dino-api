package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import java.util.ArrayList;
import java.util.List;

public class ContactSaveModel {

    private Long frontId;
    private String name;
    private List<PhoneSaveModel> phones;
    private String description;
    private String color;


    public void setFrontId(Long frontId) {
        this.frontId = frontId;
    }

    public Long getFrontId() {
        return frontId;
    }

    public void setPhones(List<PhoneSaveModel> phones) {
        this.phones = phones;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
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


