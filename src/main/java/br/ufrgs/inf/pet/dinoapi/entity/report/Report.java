package br.ufrgs.inf.pet.dinoapi.entity.report;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;

import javax.persistence.*;

import static br.ufrgs.inf.pet.dinoapi.constants.ReportConstants.WHAT_HOW_MAX;
import static br.ufrgs.inf.pet.dinoapi.constants.ReportConstants.WHERE_MAX;

@Entity
@Table(name = "report")
public class Report extends SynchronizableEntity<Long> {

    @Column(name = "what", length = WHAT_HOW_MAX, nullable = false)
    private String what;

    @Column(name = "how", length = WHAT_HOW_MAX)
    private String how;

    @Column(name = "where", length = WHERE_MAX)
    private String where;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Report() { }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getHow() {
        return how;
    }

    public void setHow(String how) {
        this.how = how;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
