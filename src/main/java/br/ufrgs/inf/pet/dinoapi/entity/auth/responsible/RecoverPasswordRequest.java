package br.ufrgs.inf.pet.dinoapi.entity.auth.responsible;

import br.ufrgs.inf.pet.dinoapi.constants.RecoverPasswordConstants;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "recover_password_request")
public class RecoverPasswordRequest {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    protected Long id;

    @Column(name = "code", length = RecoverPasswordConstants.CODE_MAX, nullable = false)
    private String code;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "attempts", nullable = false)
    private Integer attempts;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
