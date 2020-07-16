package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FakeContactService {
    private static List<Contact> fakeDB = new ArrayList<>();

    public Contact saveContact(Contact contact) {
        fakeDB.add(contact);
        return contact;
    }

    public List<Contact> getAllContacts() {
        return fakeDB;
    }

}
