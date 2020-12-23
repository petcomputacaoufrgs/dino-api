package br.ufrgs.inf.pet.dinoapi.exception.synchronizable;

import java.io.IOException;

public class SynchronizableException extends IOException {
    public SynchronizableException(String message) { super(message); }
}
