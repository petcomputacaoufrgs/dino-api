package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.constants.GoogleContactConstants;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;

import javax.persistence.*;

@Entity
@Table(name = "google_contact")
public class GoogleContact extends SynchronizableEntity<Long> {

    @Column(name = "resource_name", length = GoogleContactConstants.RESOURCE_NAME_MAX, nullable = false)
    private String resourceName;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public GoogleContact() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String name) {
        this.resourceName = name;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
