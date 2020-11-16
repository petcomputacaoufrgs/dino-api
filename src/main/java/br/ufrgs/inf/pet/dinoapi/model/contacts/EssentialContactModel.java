package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;

public class EssentialContactModel {
    private Long id;
    private Long faqId;
    private ContactModel contact;

    public EssentialContactModel(EssentialContact essContact) {
        this.setId(essContact.getId());
        Faq essContactFaq = essContact.getFaq();
        if(essContactFaq != null) {
            this.setFaqId(essContactFaq.getId());
        }
        this.setContact(new ContactModel(essContact.getContact()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFaqId() {
        return faqId;
    }

    public void setFaqId(Long faqId) {
        this.faqId = faqId;
    }

    public ContactModel getContact() {
        return contact;
    }

    public void setContact(ContactModel contact) {
        this.contact = contact;
    }
}
