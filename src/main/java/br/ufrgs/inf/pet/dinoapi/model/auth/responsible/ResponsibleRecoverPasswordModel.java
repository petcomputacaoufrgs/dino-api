package br.ufrgs.inf.pet.dinoapi.model.auth.responsible;

import br.ufrgs.inf.pet.dinoapi.constants.RecoverPasswordConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ResponsibleRecoverPasswordModel extends SetResponsibleAuthModel {
    @NotBlank(message = RecoverPasswordConstants.NULL_CODE_MESSAGE)
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
