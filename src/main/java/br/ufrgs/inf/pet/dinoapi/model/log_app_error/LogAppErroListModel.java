package br.ufrgs.inf.pet.dinoapi.model.log_app_error;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class LogAppErroListModel {

    @Valid
    @NotNull(message = "Items can't be null.")
    private List<LogAppErrorModel> items;

    public List<LogAppErrorModel> getItems() {
        return items;
    }

    public void setItems(List<LogAppErrorModel> items) {
        this.items = items;
    }
}
