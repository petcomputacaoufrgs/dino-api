package br.ufrgs.inf.pet.dinoapi.security;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DinoUserDetailsService implements UserDetailsService {

    private final UserServiceImpl userService;

    private final OAuthServiceImpl authService;

    @Autowired
    public DinoUserDetailsService(UserServiceImpl userService, OAuthServiceImpl authService) {
        this.userService = userService;
        this.authService = authService;
    }

    public DinoAuthenticationToken loadDinoUserByAuth(Auth auth) throws UsernameNotFoundException {
        if (auth != null) {
            final DinoUser dinoUser = new DinoUser(auth.getUser());
            final DinoCredentials dinoCredentials = new DinoCredentials(auth);
            return new DinoAuthenticationToken(dinoUser, dinoCredentials, new ArrayList<>());
        }

        throw new UsernameNotFoundException("Autenticação vazia.");
    }

    @Override
    public DinoUser loadUserByUsername(String email) throws UsernameNotFoundException {
        final User userDB = userService.findUserByEmail(email);

        if (userDB != null) {
            final DinoUser dinoUser = new DinoUser(userDB);

            return dinoUser;
        }

        throw new UsernameNotFoundException("Email inválido: " + email);
    }
}
