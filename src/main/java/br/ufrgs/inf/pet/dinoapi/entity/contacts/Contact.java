package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.DESCRIPTION_MAX;
import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.NAME_MAX;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contact")
public class Contact extends SynchronizableEntity<Long> {
    @Column(name = "name", length = NAME_MAX, nullable = false)
    private String name;

    @Column(name = "description", length = DESCRIPTION_MAX)
    private String description;

    @Column(name = "color")
    private Byte color;

    @ManyToOne
    @JoinColumn(name = "essential_contact_id")
    private EssentialContact essentialContact;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Phone> phones;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GoogleContact> googleContacts;

    public Contact() {
        this.phones = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<GoogleContact> getGoogleContacts() {
        return googleContacts;
    }

    public void setGoogleContacts(List<GoogleContact> googleContacts) {
        this.googleContacts = googleContacts;
    }

    public EssentialContact getEssentialContact() {
        return essentialContact;
    }

    public void setEssentialContact(EssentialContact essentialContact) {
        this.essentialContact = essentialContact;
    }
}
