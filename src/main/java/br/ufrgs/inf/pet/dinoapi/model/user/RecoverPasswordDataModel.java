package br.ufrgs.inf.pet.dinoapi.model.user;

import br.ufrgs.inf.pet.dinoapi.constants.RecoverPasswordConstants;
import br.ufrgs.inf.pet.dinoapi.constants.UserSettingsConstants;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RecoverPasswordDataModel {
    @NotNull(message = RecoverPasswordConstants.NULL_CODE_MESSAGE)
    private String code;

    @Size(min = UserSettingsConstants.PASSWORD_MIN, max = UserSettingsConstants.PASSWORD_MAX, message = UserSettingsConstants.PASSWORD_SIZE_MESSAGE)
    private String newPassword;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
