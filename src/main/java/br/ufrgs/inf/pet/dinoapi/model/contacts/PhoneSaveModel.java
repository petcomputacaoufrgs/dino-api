package br.ufrgs.inf.pet.dinoapi.model.contacts;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.*;

public class PhoneSaveModel {

    @NotNull(message = NUMBER_NULL_MESSAGE)
    @Size(max = NUMBER_MAX)
    private String number;

    @NotNull(message = TYPE_NULL_MESSAGE)
    @Size(max = TYPE_MAX)
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


