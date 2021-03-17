package br.ufrgs.inf.pet.dinoapi.exception;

import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;

public class ResponsibleRecoverRequestMaxAttemptsException extends Exception {
    public ResponsibleRecoverRequestMaxAttemptsException() { super(AuthConstants.RECOVER_REQUEST_MAX_ATTEMPTS_ERROR); }
}
