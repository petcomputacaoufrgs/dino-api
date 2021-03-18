package br.ufrgs.inf.pet.dinoapi.model.auth.responsible;

import br.ufrgs.inf.pet.dinoapi.constants.UserSettingsConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SetResponsibleAuthModel {
    @NotBlank(message = UserSettingsConstants.KEY_NULL_MESSAGE)
    @Size(min = UserSettingsConstants.KEY_MIN, max = UserSettingsConstants.KEY_MAX, message = UserSettingsConstants.KEY_SIZE_MESSAGE)
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
