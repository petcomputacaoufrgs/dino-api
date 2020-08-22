package br.ufrgs.inf.pet.dinoapi.entity.faq;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "faqs_version")
public class FaqsVersion implements Serializable {

        private static final long serialVersionUID = 1L;

        private static final String SEQUENCE_NAME = "faqs_version_seq";

        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
        @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
        @Column(name = "id")
        private Long id;

        @NotNull(message = "Versão não pode ser nula.")
        @Column(name = "version")
        private Long version;

        public FaqsVersion() {
            this.setVersion(1L);
        }

        public FaqsVersion(Long version) {
            this.setVersion(version);
        }

        public Long getId() {
            return id;
        }

        public Long getVersion() {
            return version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

        public void updateVersion() {
            this.version = version + 1L;
        }
}
