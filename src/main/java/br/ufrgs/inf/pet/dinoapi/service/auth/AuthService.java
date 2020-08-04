package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.entity.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    Auth refreshAuth(Auth auth, HttpServletRequest request);

    Auth generateAuth(User user, HttpServletRequest request);

    Auth findByAccessToken(String accessToken);

    Auth getCurrentAuth();

    User getCurrentUser();

    UserDetails getPrincipal();

    ResponseEntity<?> logout();

}
