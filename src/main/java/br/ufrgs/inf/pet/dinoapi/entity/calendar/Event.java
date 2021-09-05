package br.ufrgs.inf.pet.dinoapi.entity.calendar;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.DESCRIPTION_MAX;
import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.NAME_MAX;

@Entity
@Table(name = "event")
public class Event extends SynchronizableEntity<Long> {

    @Column(name = "title", length = NAME_MAX, nullable = false)
    private String title;

    @Column(name = "description", length = DESCRIPTION_MAX)
    private String description;

    @Column(name = "beginTime", length = 2)
    private String beginTime;

    @Column(name = "endTime", length = 2)
    private String endTime;

    @OneToOne
    @JoinColumn(name = "type_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private EventType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
