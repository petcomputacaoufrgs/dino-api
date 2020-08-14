package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactSaveModel;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.*;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "contact")
public class Contact implements Serializable {

        private static final long serialVersionUID = 1L;

        private static final String SEQUENCE_NAME = "contact_seq";

        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
        @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
        @Column(name = "id", nullable = false)
        private Long id;

        @Column(name = "name", length = NAME_MAX, nullable = false)
        private String name;

        @Valid
        @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL)
        private List<Phone> phones;

        @Column(name = "description", length = DESCRIPTION_MAX)
        private String description;

        @Column(name = "color", length = COLOR_MAX)
        private String color;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        public Contact() {
                this.phones = new ArrayList<>();
        }

        public Contact(ContactSaveModel model, User user){
                this.setName(model.getName());
                this.setDescription(model.getDescription());
                this.setColor(model.getColor());
                this.setUser(user);
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

        public String getColor() {
                return color;
        }

        public void setColor (String color) { this.color = color; }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

}
