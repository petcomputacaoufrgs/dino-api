package br.ufrgs.inf.pet.dinoapi.service.user;


import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        if (email != null) {
            final Optional<User> queryResult = userRepository.findByEmail(email);
            if (queryResult.isPresent()) {
                return queryResult.get();
            }
        }

        return null;
    }
}
