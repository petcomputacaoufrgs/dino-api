package br.ufrgs.inf.pet.dinoapi.exception;

import br.ufrgs.inf.pet.dinoapi.constants.ResponsibleAuthConstants;

public class ResponsibleRecoverRequestMaxAttemptsException extends Exception {
    public ResponsibleRecoverRequestMaxAttemptsException() { super(ResponsibleAuthConstants.RECOVER_REQUEST_MAX_ATTEMPTS_ERROR); }
}
