package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.Auth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório da entidade: {@link Auth}
 *
 * @author joao.silva
 */
@Repository
public interface AuthRepository extends CrudRepository<Auth, Long> {
    @Query("SELECT a FROM Auth a WHERE a.accessToken LIKE :accessToken")
    Optional<Auth> findByAccessToken(@Param("accessToken") String accessToken);
}
