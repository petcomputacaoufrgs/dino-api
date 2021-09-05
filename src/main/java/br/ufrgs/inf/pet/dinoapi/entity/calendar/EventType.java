package br.ufrgs.inf.pet.dinoapi.entity.calendar;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.DESCRIPTION_MAX;
import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.NAME_MAX;

@Entity
@Table(name = "event_type")
public class EventType extends SynchronizableEntity<Long> {

    @Column(name = "title", length = NAME_MAX, nullable = false)
    private String title;

    @Column(name = "color", length = DESCRIPTION_MAX)
    private String color;

    @Column(name = "icon", length = DESCRIPTION_MAX)
    private String icon;

    @OneToOne(mappedBy = "type")
    private Event event;
}
