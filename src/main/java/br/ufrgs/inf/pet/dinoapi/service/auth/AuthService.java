package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.entity.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshRequestModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    Auth generateAuth(User user);

    ResponseEntity<?> refreshAuth(AuthRefreshRequestModel authRefreshRequestModel);

    Auth findByAccessToken(String accessToken);

    Auth getCurrentAuth();

    User getCurrentUser();

    UserDetails getPrincipal();

    ResponseEntity<?> logout();

}
