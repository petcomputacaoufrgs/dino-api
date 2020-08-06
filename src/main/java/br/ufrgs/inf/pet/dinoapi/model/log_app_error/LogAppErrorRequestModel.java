package br.ufrgs.inf.pet.dinoapi.model.log_app_error;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LogAppErrorRequestModel {

    @Size(max = 500, message = "title should not be more than 500.")
    private String title;

    @Size(max = 500, message = "file should not be more than 500.")
    private String file;

    @Size(max = 10000, message = "error should not be more than 10000.")
    @NotNull(message = "error cannot be null.")
    private String error;

    @NotNull(message = "date cannot be null.")
    private Long date;

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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
