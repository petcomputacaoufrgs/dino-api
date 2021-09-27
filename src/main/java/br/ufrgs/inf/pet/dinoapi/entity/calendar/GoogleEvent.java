package br.ufrgs.inf.pet.dinoapi.entity.calendar;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "google_event")
public class GoogleEvent {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    protected Long id;

    @Column(name = "google_id", nullable = false)
    protected String googleId;

    @OneToOne
    @JoinColumn(name = "event_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
