package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "contact", uniqueConstraints={
        @UniqueConstraint(columnNames={"id", "user_id"})
})
public class Contact implements Serializable {

        private static final long serialVersionUID = 1L;

        private static final String SEQUENCE_NAME = "contact_seq";

        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
        @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
        @Basic(optional = false)
        @Column(name = "id")
        private Long id;

        @Basic(optional = false)
        @NotNull(message = "O front id do contato não pode ser nulo.")
        @Column(name = "frontId")
        private Long frontId;

        @Basic(optional = false)
        @NotNull(message = "O nome do contato não pode ser nulo.")
        @Size(min = 1, max = 50)
        @Column(name = "name", length = 50)
        private String name;

        @Valid
        @OneToMany(mappedBy = "contact")
        private List<Phone> phones;

        @Size(max = 500)
        @Column(name = "description", length = 500)
        private String description;

        @Size(min = 1, max = 10)
        @Column(name = "color", length = 10)
        private String color;

        @JsonIgnore
        @Valid
        @ManyToOne
        @NotNull(message = "O usuário associado não pode ser nulo.")
        @JoinColumn(name = "user_id")
        private User user;

        public Contact() {
                this.phones = new ArrayList<>();
        }

        public Contact(ContactSaveModel model, User user){
                this.setFrontId(model.getFrontId());
                this.setName(model.getName());
                this.setDescription(model.getDescription());
                this.setColor(model.getColor());
                this.setUser(user);
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) { this.id = id; }

        public Long getFrontId() {
                return frontId;
        }

        public void setFrontId(Long frontId) {
                this.frontId = frontId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Phone> getPhones() { return phones; }

        public void setPhones(List<Phone> phones) { this.phones = phones; }

        public void addPhone(Phone phone) { this.phones.add(phone); }

        public void addPhones(List<Phone> phones) { this.phones.addAll(phones); }

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
