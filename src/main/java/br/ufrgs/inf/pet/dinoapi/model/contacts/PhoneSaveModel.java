package br.ufrgs.inf.pet.dinoapi.model.contacts;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;

public class PhoneSaveModel {

    private String number;
    private byte type;

    public PhoneSaveModel() {}

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}


