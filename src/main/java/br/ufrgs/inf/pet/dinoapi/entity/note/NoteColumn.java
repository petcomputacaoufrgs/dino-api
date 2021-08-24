package br.ufrgs.inf.pet.dinoapi.entity.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "note_column")
public class NoteColumn extends SynchronizableEntity<Long> {
    @Column(name = "title", length = NoteConstants.COLUMN_TITLE_MAX, nullable = false)
    private String title;

    @Column(name = "n_order", nullable = false)
    private Integer order;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToMany(mappedBy = "noteColumn", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Note> notes;

    public NoteColumn() { }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
