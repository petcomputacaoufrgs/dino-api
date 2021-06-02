package br.ufrgs.inf.pet.dinoapi.entity.kids_space;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static br.ufrgs.inf.pet.dinoapi.constants.KidsSpaceConstants.COLOR_MAX;

@Entity
@Table(name = "kids_space_settings")
public class KidsSpaceSettings extends SynchronizableEntity<Long> {
    @Column(name = "first_settings_done", nullable = false)
    private Boolean firstSettingsDone;

    @Column(name = "color", length = COLOR_MAX)
    private String color;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public KidsSpaceSettings() { }

    public Boolean getFirstSettingsDone() {
        return firstSettingsDone;
    }

    public void setFirstSettingsDone(Boolean firstSettingsDone) {
        this.firstSettingsDone = firstSettingsDone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
