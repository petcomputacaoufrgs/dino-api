package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório da entidade: {@link User}
 *
 * @author joao.silva
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByGoogleAuthId(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByAccessToken(String accessToken);

}
