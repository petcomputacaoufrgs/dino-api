package br.ufrgs.inf.pet.dinoapi.entity.log_error;

import javax.persistence.*;
import java.time.LocalDateTime;

import static br.ufrgs.inf.pet.dinoapi.constants.LogAppErrorConstants.*;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "log_api_error")
public class LogAPIError {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    protected Long id;

    @Column(name = "file", length = FILE_MAX)
    private String className;

    @Column(name = "error", length = ERROR_MAX, nullable = false)
    private String error;

    @Column(name = "log_date", nullable = false)
    private LocalDateTime date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String file) {
        this.className = file;
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
}
