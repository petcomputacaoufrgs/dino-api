package br.ufrgs.inf.pet.dinoapi.security;

import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class DinoUserDetailsService implements UserDetailsService {

    private final UserServiceImpl userService;

    @Autowired
    public DinoUserDetailsService(UserServiceImpl userService) {
        this.userService = userService;
    }

    public DinoAuthenticationToken loadDinoUserByAuth(Auth auth) throws UsernameNotFoundException {
        if (auth != null) {
            final User user = auth.getUser();
            final DinoGrantedAuthority dinoAuthority = new DinoGrantedAuthority(user.getPermission());
            final DinoCredentials dinoCredentials = new DinoCredentials(auth);
            return new DinoAuthenticationToken(user, dinoCredentials, Collections.singletonList(dinoAuthority));
        }

        throw new UsernameNotFoundException(AuthConstants.EMPTY_AUTH);
    }

    @Override
    public DinoUser loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = userService.findUserByEmail(email);

        if (user != null) {
            final DinoGrantedAuthority dinoAuthority = new DinoGrantedAuthority(user.getPermission());
            return new DinoUser(user, Collections.singletonList(dinoAuthority));
        }

        throw new UsernameNotFoundException(AuthConstants.INVALID_EMAIL + ": " + email);
    }
}
