package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.DESCRIPTION_MAX;
import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.NAME_MAX;

@Entity
@Table(name = "contact")
public class Contact extends SynchronizableEntity<Long> {

        @Column(name = "name", length = NAME_MAX, nullable = false)
        private String name;

        @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<Phone> phones;

        @Column(name = "description", length = DESCRIPTION_MAX)
        private String description;

        @Column(name = "color")
        private Byte color;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @OneToMany(mappedBy = "contact")
        private List<EssentialContact> essentialContacts;

        @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private List<EssentialContactMapping> essentialContactMappings;

        @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private List<GoogleContact> googleContacts;

        public Contact() {
                this.phones = new ArrayList<>();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) { this.id = id; }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Phone> getPhones() { return phones; }

        public void setPhones(List<Phone> phones) { this.phones = phones; }

        public String getDescription() {
            return description;
        }

        public void setDescription (String description) { this.description = description; }

        public Byte getColor() {
                return color;
        }

        public void setColor (Byte color) { this.color = color; }

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
}
