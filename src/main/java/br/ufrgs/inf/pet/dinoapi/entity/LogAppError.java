package br.ufrgs.inf.pet.dinoapi.entity;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "log_app_error")
public class LogAppError {
    private static final String SEQUENCE_NAME = "log_app_error_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "file", length = 200)
    private String file;

    @Column(name = "error", nullable = false, length = 10000)
    private String error;

    @Column(name = "log_date", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "auth_id", nullable = false)
    private Auth auth;

    public LogAppError() {
        this.date = new Date();
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }
}
