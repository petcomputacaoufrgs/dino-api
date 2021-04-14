package br.ufrgs.inf.pet.dinoapi.entity.auth.google;

import br.ufrgs.inf.pet.dinoapi.constants.GoogleAuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;

import javax.persistence.*;

@Entity
@Table(name = "google_scope")
public class GoogleScope extends SynchronizableEntity<Long> {
    @Column(name = "name", length = GoogleAuthConstants.GOOGLE_SCOPE_MAX, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "google_auth_id", nullable = false)
    private GoogleAuth googleAuth;

    public GoogleScope() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GoogleAuth getGoogleAuth() {
        return googleAuth;
    }

    public void setGoogleAuth(GoogleAuth googleAuth) {
        this.googleAuth = googleAuth;
    }
}
