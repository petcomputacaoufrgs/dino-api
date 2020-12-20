package br.ufrgs.inf.pet.dinoapi.repository.user;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
