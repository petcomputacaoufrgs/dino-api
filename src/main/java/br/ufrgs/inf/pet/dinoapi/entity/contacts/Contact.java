package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.lang.reflect.Array;
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
        @NotNull(message = "O nome do contato não pode ser nulo.")
        @Size(min = 1, max = 50)
        @Column(name = "name", length = 50)
        private String name;

        @Valid
        @OneToMany(mappedBy = "phone")
        private ArrayList<Phone> phones;

        @Size(min = 1, max = 500)
        @Column(name = "description", length = 500)
        private String description;

        @JsonIgnore
        @Valid
        @ManyToOne
        @NotNull(message = "O usuário associado não pode ser nulo.")
        @JoinColumn(name = "user_id")
        private User user;

        public Contact() {}

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

        public ArrayList<Phone> getPhones() { return phones; }

        public void setPhones(ArrayList<Phone> phones) { this.phones = phones; }

        public String getDescription() {
            return description;
        }

        public void setDescription (String description) { this.description = description; }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public void setByContactSaveModel(ContactSaveModel contactSaveModel) {
                this.setName(contactSaveModel.getName());
                this.setPhonesByPhoneSaveModel(contactSaveModel.getPhones());
                this.setDescription(contactSaveModel.getDescription());
        }

        private void setPhonesByPhoneSaveModel(List<PhoneSaveModel> phoneSaveModelList) {
                if (phoneSaveModelList.size() > 0) {
                        for (PhoneSaveModel model : phoneSaveModelList) {
                                Phone phone = new Phone();
                                phone.setPhoneByPhoneSaveModel(model);
                                this.phones.add(phone);
                        }
                }
        }


}
