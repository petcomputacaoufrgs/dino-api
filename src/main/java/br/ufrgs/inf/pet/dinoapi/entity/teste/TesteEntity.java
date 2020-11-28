package br.ufrgs.inf.pet.dinoapi.entity.teste;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "teste")
public class TesteEntity extends SynchronizableEntity {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    protected Long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "last_update", nullable = false)
    protected LocalDateTime lastUpdate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public LocalDateTime getLastUpdate() {
        return this.lastUpdate;
    }

    @Override
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public Long getId() {
        return id;
    }
}
