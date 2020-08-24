package br.ufrgs.inf.pet.dinoapi.model.contacts;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.*;

public class PhoneSaveModel {

    @NotNull(message = NUMBER_NULL_MESSAGE)
    @Size(min = 1, max = NUMBER_MAX, message = NUMBER_MESSAGE)
    private String number;

    @NotNull(message = TYPE_NULL_MESSAGE)
    @Range(min = 1, max = TYPE_MAX, message = TYPE_MESSAGE)
    private short type;

    public PhoneSaveModel() {}

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}


