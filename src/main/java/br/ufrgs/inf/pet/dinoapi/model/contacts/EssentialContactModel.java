package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;

public class EssentialContactModel {
    private Long id;
    private Long faqId;
    private ContactModel contact;

    public EssentialContactModel(EssentialContact eContact) {
        this.setId(eContact.getId());
        Faq eContactFaq = eContact.getFaq();
        if(eContactFaq != null) {
            this.setFaqId(eContactFaq.getId());
        }
        this.setContact(new ContactModel(eContact.getContact()));
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
