package br.ufrgs.inf.pet.dinoapi.exception.synchronizable;

import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;

public class UserWithoutPermissionException extends Exception {
    public UserWithoutPermissionException() { super(AuthConstants.WITHOUT_PERMISSION); }
}
