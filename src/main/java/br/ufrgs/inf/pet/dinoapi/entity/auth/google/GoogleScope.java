package br.ufrgs.inf.pet.dinoapi.entity.auth.google;

import br.ufrgs.inf.pet.dinoapi.constants.GoogleAuthConstants;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "google_scope")
public class GoogleScope {
    private static final String SEQUENCE_NAME = "google_scope_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = GoogleAuthConstants.GOOGLE_SCOPE_MAX, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "google_scope", nullable = false)
    private GoogleAuth googleAuth;

    public GoogleScope() { }

    public GoogleScope(GoogleAuth googleAuth, String name) {
        this.name = name;
        this.googleAuth = googleAuth;
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
}
