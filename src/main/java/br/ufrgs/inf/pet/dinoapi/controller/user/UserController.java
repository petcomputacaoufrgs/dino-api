package br.ufrgs.inf.pet.dinoapi.controller.user;

import org.springframework.http.ResponseEntity;

public interface UserController {
    /**
     * Delete an user account
     * @return if success return true, else return false
     */
    ResponseEntity<Boolean> deleteAccount();
}
