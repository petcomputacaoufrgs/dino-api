package br.ufrgs.inf.pet.dinoapi.service.user;

import org.springframework.http.ResponseEntity;

public interface UserService {
    /**
     * Delete current user
     * @return if success return true else false
     */
    ResponseEntity<Boolean> deleteAccount();
}
