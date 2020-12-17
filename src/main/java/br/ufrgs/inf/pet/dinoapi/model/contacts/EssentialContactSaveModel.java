package br.ufrgs.inf.pet.dinoapi.model.contacts;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.*;
import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.DESCRIPTION_MAX_MESSAGE;

public class EssentialContactSaveModel {

    private List<Long> faqIds;

    @NotNull(message = NAME_NULL_MESSAGE)
    @Size(min = 1, max = NAME_MAX, message = NAME_MAX_MESSAGE)
    private String name;

    @Valid
    private List<PhoneSaveModel> phones;

    @Size(max = DESCRIPTION_MAX, message = DESCRIPTION_MAX_MESSAGE)
    private String description;

    private Byte color;

    public EssentialContactSaveModel(List<Long> faqIds, @NotNull(message = NAME_NULL_MESSAGE) @Size(min = 1, max = NAME_MAX, message = NAME_MAX_MESSAGE) String name, @Valid List<PhoneSaveModel> phones, @Size(max = DESCRIPTION_MAX, message = DESCRIPTION_MAX_MESSAGE) String description, Byte color) {
        this.faqIds = faqIds;
        this.name = name;
        this.phones = phones;
        this.description = description;
        this.color = color;
    }

    public List<Long> getFaqIds() {
        return faqIds;
    }

    public void setFaqIds(List<Long> faqIds) {
        this.faqIds = faqIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PhoneSaveModel> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneSaveModel> phones) {
        this.phones = phones;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getColor() {
        return color;
    }

    public void setColor(Byte color) {
        this.color = color;
    }
}
