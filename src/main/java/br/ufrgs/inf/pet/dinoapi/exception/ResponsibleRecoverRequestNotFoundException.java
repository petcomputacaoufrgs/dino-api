package br.ufrgs.inf.pet.dinoapi.exception;

import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;

public class ResponsibleRecoverRequestNotFoundException extends Exception {
    public ResponsibleRecoverRequestNotFoundException() { super(AuthConstants.RECOVER_REQUEST_FOUND_NOT_FOUND_ERROR); }
}
