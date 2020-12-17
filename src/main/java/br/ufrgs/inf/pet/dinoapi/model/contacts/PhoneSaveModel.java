package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.*;

public class PhoneSaveModel {

    @NotNull(message = NUMBER_NULL_MESSAGE)
    @Size(min = 1, max = NUMBER_MAX, message = NUMBER_MESSAGE)
    private String number;

    private short type;

    public PhoneSaveModel() {}

    public PhoneSaveModel(@NotNull(message = NUMBER_NULL_MESSAGE) @Size(min = 1, max = NUMBER_MAX, message = NUMBER_MESSAGE) String number,
                          @NotNull(message = TYPE_NULL_MESSAGE) @Range(min = 1, max = TYPE_MAX, message = TYPE_MESSAGE) Byte type) {
        this.number = number;
        this.type = type;
    }

    public PhoneSaveModel(Phone phone) {
        this.number = phone.getNumber();
        this.type = phone.getType();
    }

    public short getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}


