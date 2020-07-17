package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;

public class PhoneModel {
    private Long id;
    private String number;
    private byte type;

    /*
    1: special short public utility numbers (see below)
    2 to 5: landlines
    6 to 9: mobile phones
    */

    public PhoneModel(){}

    public PhoneModel(Phone phone) {
        this.id = phone.getId();
        this.number = phone.getNumber();
        this.type = phone.getType();
    }

    public Long getId() { return id; }

    public void setId(Long id) {this.id = id;}

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
