package br.ufrgs.inf.pet.dinoapi.exception;

import br.ufrgs.inf.pet.dinoapi.constants.ResponsibleAuthConstants;

public class ResponsibleRecoverRequestNotFoundException extends Exception {
    public ResponsibleRecoverRequestNotFoundException() { super(ResponsibleAuthConstants.RECOVER_REQUEST_FOUND_NOT_FOUND_ERROR); }
}
