package br.ufrgs.inf.pet.dinoapi.model.log_app_error;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

import static br.ufrgs.inf.pet.dinoapi.constants.LogAppErrorConstants.*;

public class LogAppErrorRequestModel {

    @Size(max = TITLE_MAX, message = TITLE_MESSAGE)
    private String title;

    @Size(max = FILE_MAX, message = FILE_MESSAGE)
    private String file;

    @Size(max = ERROR_MAX, message = ERROR_MESSAGE)
    @NotNull(message = ERROR_NULL_MESSAGE)
    private String error;

    @NotNull(message = DATE_NULL_MESSAGE)
    private ZonedDateTime date;

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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}
