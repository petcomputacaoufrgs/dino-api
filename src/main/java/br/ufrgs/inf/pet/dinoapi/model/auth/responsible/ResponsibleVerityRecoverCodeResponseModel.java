package br.ufrgs.inf.pet.dinoapi.model.auth.responsible;

public class ResponsibleVerityRecoverCodeResponseModel {
    private Boolean valid;
    private Boolean requestNotFound;

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean getRequestNotFound() {
        return requestNotFound;
    }

    public void setRequestNotFound(Boolean requestNotFound) {
        this.requestNotFound = requestNotFound;
    }
}
