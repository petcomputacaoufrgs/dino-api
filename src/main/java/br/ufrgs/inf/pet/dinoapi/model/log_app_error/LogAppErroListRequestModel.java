package br.ufrgs.inf.pet.dinoapi.model.log_app_error;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class LogAppErroListRequestModel {

    @Valid
    @NotNull(message = "items can't be null.")
    private List<LogAppErrorRequestModel> items;

    public LogAppErroListRequestModel() {
        this.items = new ArrayList<>();
    }

    public List<LogAppErrorRequestModel> getItems() {
        return items;
    }

    public void setItems(List<LogAppErrorRequestModel> items) {
        this.items = items;
    }
}
