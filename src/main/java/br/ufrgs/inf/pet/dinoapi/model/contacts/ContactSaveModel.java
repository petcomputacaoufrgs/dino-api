package br.ufrgs.inf.pet.dinoapi.model.contacts;

import java.util.ArrayList;

public class ContactSaveModel {
    private String name;
    private ArrayList<PhoneSaveModel> phones;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<PhoneSaveModel> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<PhoneSaveModel> phones) {
        this.phones = phones;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
