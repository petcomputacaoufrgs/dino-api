package br.ufrgs.inf.pet.dinoapi.model.contacts;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.*;

public class ContactSaveModel {

    private Long frontId;

    @NotNull(message = NAME_NULL_MESSAGE)
    @Size(min = 1, max = NAME_MAX, message = NAME_MAX_MESSAGE)
    private String name;

    @Valid
    private List<PhoneSaveModel> phones;

    @Size(max = DESCRIPTION_MAX, message = DESCRIPTION_MAX_MESSAGE)
    private String description;

    private Byte color;

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

    public void setColor(Byte color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Byte getColor() {
        return color;
    }
}


