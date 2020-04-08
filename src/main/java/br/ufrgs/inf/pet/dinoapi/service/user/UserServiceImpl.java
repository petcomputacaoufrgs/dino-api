package br.ufrgs.inf.pet.dinoapi.service.user;


import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    public ResponseEntity<?> create(User user) {
        if (user != null && user.getExternalId() != null) {
            User userDB = this.findOneUserByExternalId(user.getExternalId());
            if (userDB == null) {
                userDB = userRepository.save(user);
                return new ResponseEntity<>(userDB, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Email já cadastrado.", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Usuário e email não poden ser nulos.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findOneUserByAccessToken(String token) {
        if (token != null && token != "") {
            return userRepository.findFirstByAccessToken(token).get();
        }
        return null;
    }

    @Override
    public User findOneUserByExternalId(String externalId) {
        if (externalId != null) {
            return userRepository.findByExternalId(externalId).get();
        }
        return null;
    }


}
