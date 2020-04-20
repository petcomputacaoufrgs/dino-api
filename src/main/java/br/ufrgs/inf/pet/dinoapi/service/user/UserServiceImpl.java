package br.ufrgs.inf.pet.dinoapi.service.user;


import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementação de {@link UserService}
 *
 * @author joao.silva
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByAccessToken(String accessToken) {
        Optional<User> userSearchResult = userRepository.findByAccessToken(accessToken);

        if (userSearchResult.isPresent()) {
           return userSearchResult.get();
        }

        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        if (email != null) {
            Optional<User> queryResult = userRepository.findByEmail(email);
            if (queryResult.isPresent()) {
                return queryResult.get();
            }
        }

        return null;
    }

    @Override
    public User getCurrentUser() {
        SecurityContext context =  SecurityContextHolder.getContext();

        if (context == null) {
            return null;
        }

        Authentication auth = context.getAuthentication();

        if (auth == null) {
            return null;
        }

        UserDetails userDetais = (UserDetails) auth.getPrincipal();

        String email = userDetais.getUsername();

        return findUserByEmail(email);
    }

}
