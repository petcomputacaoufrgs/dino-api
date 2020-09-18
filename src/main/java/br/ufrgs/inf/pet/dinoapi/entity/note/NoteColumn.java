package br.ufrgs.inf.pet.dinoapi.entity.note;

import br.ufrgs.inf.pet.dinoapi.entity.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "note_column", uniqueConstraints={
        @UniqueConstraint(columnNames={"title", "user_id"})
})
public class NoteColumn {
    private static final String SEQUENCE_NAME = "note_column_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "n_order", nullable = false)
    private Integer order;

    @Column(name = "last_update_day", nullable = false)
    private Date lastUpdate;

    @Column(name = "last_order_update", nullable = false)
    private Date lastOrderUpdate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "noteColumn", cascade = CascadeType.ALL)
    private List<Note> notes;

    public NoteColumn() {}

    public NoteColumn(User user, Integer order, Date lastOrderUpdate) {
        this.user = user;
        this.order = order;
        this.lastOrderUpdate = lastOrderUpdate;
    }

    public Long getId() {
        return id;
    }

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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public Date getLastOrderUpdate() {
        return lastOrderUpdate;
    }

    public void setLastOrderUpdate(Date lastOrderUpdate) {
        this.lastOrderUpdate = lastOrderUpdate;
    }
}
