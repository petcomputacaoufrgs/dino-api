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

    @Column(name = "message", length = MESSAGE_MAX, nullable = false)
    private String message;

    @Column(name = "stack_trace", length = STACK_TRACE_MAX, nullable = false)
    private String stackTract;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTract() {
        return stackTract;
    }

    public void setStackTract(String stackTract) {
        this.stackTract = stackTract;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
