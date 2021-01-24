package br.ufrgs.inf.pet.dinoapi.entity.auth;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff")
public class Staff extends SynchronizableEntity<Long> {

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "sent_invitation_date")
    private LocalDateTime sentInvitationDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Staff() {}

    public Staff(String email, LocalDateTime sentInvitationDate, User user) {
        this.email = email;
        this.sentInvitationDate = sentInvitationDate;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getSentInvitationDate() {
        return sentInvitationDate;
    }

    public void setSentInvitationDate(LocalDateTime sentInvitationDate) {
        this.sentInvitationDate = sentInvitationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
