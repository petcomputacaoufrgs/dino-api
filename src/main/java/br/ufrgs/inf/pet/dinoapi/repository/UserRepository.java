package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório da entidade: {@link User}
 *
 * @author joao.silva
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findOneByExternalId(String externalId);

    User findFirstByAccessToken(String accessToken);

}
