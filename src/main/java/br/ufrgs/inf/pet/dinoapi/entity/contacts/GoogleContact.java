package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.constants.GoogleContactConstants;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;

import javax.persistence.*;

@Entity
@Table(name = "google_contact")
public class GoogleContact extends SynchronizableEntity<Long> {

    @Column(name = "resource_name", length = GoogleContactConstants.RESOURCE_NAME_MAX)
    private String resourceName;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Boolean savedOnGoogleAPI;

    public GoogleContact() { }

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

    public Boolean getSavedOnGoogleAPI() {
        return savedOnGoogleAPI;
    }

    public void setSavedOnGoogleAPI(Boolean savedOnGoogleAPI) {
        this.savedOnGoogleAPI = savedOnGoogleAPI;
    }
}
