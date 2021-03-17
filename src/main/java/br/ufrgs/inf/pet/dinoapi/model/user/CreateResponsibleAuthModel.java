package br.ufrgs.inf.pet.dinoapi.model.user;

import br.ufrgs.inf.pet.dinoapi.constants.UserSettingsConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateResponsibleAuthModel {
    @NotNull(message = UserSettingsConstants.PASSWORD_NULL_MESSAGE)
    @Size(min = UserSettingsConstants.PASSWORD_MIN, max = UserSettingsConstants.PASSWORD_MAX, message = UserSettingsConstants.PASSWORD_SIZE_MESSAGE)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
