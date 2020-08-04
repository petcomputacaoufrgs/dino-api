package br.ufrgs.inf.pet.dinoapi.service.user_details;

import br.ufrgs.inf.pet.dinoapi.entity.Auth;
import br.ufrgs.inf.pet.dinoapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class DinoUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public DinoUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    public UserDetails loadUserDetailsByAuth(Auth auth) throws UsernameNotFoundException {
        if (auth != null) {
            return new User(auth.getUser().getEmail(), auth.getAccessToken(), new ArrayList<>());
        }

        throw new UsernameNotFoundException("Autenticação vazia.");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final br.ufrgs.inf.pet.dinoapi.entity.User userDB = userService.findUserByEmail(email);

        if (userDB != null) {
            return new User(userDB.getEmail(), userDB.getAccessToken(), new ArrayList<>()); 
        }

        throw new UsernameNotFoundException("Email inválido: " + email);
    }
}
