package br.ufrgs.inf.pet.dinoapi.model.auth.google;

import br.ufrgs.inf.pet.dinoapi.constants.GoogleAuthConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class GoogleGrantRequestModel extends  GoogleAuthRequestModel {
    @NotNull
    @Size(min = 1, max = 10, message = GoogleAuthConstants.SCOPE_LIST_MAX_SIZE)
    private List<String> scopeList;

    public GoogleGrantRequestModel() {}

    public List<String> getScopeList() {
        return scopeList;
    }

    public void setScopeList(List<String> scopeList) {
        this.scopeList = scopeList;
    }
}
