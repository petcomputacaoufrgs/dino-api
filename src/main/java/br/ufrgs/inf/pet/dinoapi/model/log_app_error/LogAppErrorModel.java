package br.ufrgs.inf.pet.dinoapi.model.log_app_error;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LogAppErrorModel {

    @Size(max = 100, message = "Title should not be more than 100")
    private String title;

    @Size(max = 200, message = "File should not be more than 200")
    private String file;

    @Size(max = 10000, message = "Error should not be more than 10000")
    @NotNull(message = "Error cannot be null")
    private String error;

    @NotNull(message = "Date cannot be null.")
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
