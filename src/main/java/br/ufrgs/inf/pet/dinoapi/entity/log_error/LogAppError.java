package br.ufrgs.inf.pet.dinoapi.entity.log_error;

import javax.persistence.*;
import java.time.LocalDateTime;
import static br.ufrgs.inf.pet.dinoapi.constants.LogAppErrorConstants.*;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "log_app_error")
public class LogAppError {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    protected Long id;

    @Column(name = "title", length = TITLE_MAX)
    private String title;

    @Column(name = "file", length = FILE_MAX)
    private String file;

    @Column(name = "error", length = STACK_TRACE_MAX, nullable = false)
    private String error;

    @Column(name = "log_date", nullable = false)
    private LocalDateTime date;

    @Column(name = "user_agent", nullable = false)
    private String userAgent;

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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
