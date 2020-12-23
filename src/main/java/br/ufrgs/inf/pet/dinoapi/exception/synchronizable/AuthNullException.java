package br.ufrgs.inf.pet.dinoapi.exception.synchronizable;

import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;

public class AuthNullException extends SynchronizableException {
    public AuthNullException() { super(AuthConstants.INVALID_AUTH); }
}
