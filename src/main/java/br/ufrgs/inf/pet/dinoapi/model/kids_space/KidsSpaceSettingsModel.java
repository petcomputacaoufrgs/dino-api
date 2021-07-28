package br.ufrgs.inf.pet.dinoapi.model.kids_space;

import br.ufrgs.inf.pet.dinoapi.constants.KidsSpaceConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class KidsSpaceSettingsModel extends SynchronizableDataLocalIdModel<Long> {
    @NotNull(message = KidsSpaceConstants.FIRST_SETTING_DONE_NULL_MESSAGE)
    private Boolean firstSettingsDone;

    @NotBlank(message = KidsSpaceConstants.COLOR_BLANK_MESSAGE)
    private String color;

    private String hat;

    public String getHat() {
        return hat;
    }

    public void setHat(String hat) {
        this.hat = hat;
    }

    public Boolean getFirstSettingsDone() {
        return firstSettingsDone;
    }

    public void setFirstSettingsDone(Boolean firstSettingsDone) {
        this.firstSettingsDone = firstSettingsDone;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
