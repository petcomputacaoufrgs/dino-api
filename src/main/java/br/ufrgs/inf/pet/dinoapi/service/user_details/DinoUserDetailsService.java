package br.ufrgs.inf.pet.dinoapi.service.user_details;

import br.ufrgs.inf.pet.dinoapi.service.user.UserService;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DinoUserDetailsService implements UserDetailsService {
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        br.ufrgs.inf.pet.dinoapi.entity.User userDB = userService.findUserByEmail(email);

        if (userDB != null) {
            return new User(userDB.getEmail(), userDB.getAccessToken(), new ArrayList<>()); //USER DO SPRING N NOSSO -> user, senha e permissões mas na real é email token e []
        }

        throw new UsernameNotFoundException(email);
    }
}
