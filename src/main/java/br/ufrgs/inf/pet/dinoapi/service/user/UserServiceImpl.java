package br.ufrgs.inf.pet.dinoapi.service.user;


import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


}
