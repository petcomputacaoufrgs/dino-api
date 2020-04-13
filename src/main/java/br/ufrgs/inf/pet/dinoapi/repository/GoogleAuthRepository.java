package br.ufrgs.inf.pet.dinoapi.repository;


import br.ufrgs.inf.pet.dinoapi.entity.GoogleAuth;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório da entidade: {@link GoogleAuth}
 *
 * @author joao.silva
 */
@Repository
public interface GoogleAuthRepository extends CrudRepository<GoogleAuth, Long>  {

    Optional<GoogleAuth> findByGoogleId(String googleId);

}
