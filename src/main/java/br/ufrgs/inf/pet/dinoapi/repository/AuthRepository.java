package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRepository extends CrudRepository<Auth, Long> {
    @Query("SELECT a FROM Auth a WHERE a.accessToken = :accessToken")
    Optional<Auth> findByAccessToken(@Param("accessToken") String accessToken);

    @Query("SELECT a FROM Auth a WHERE a.webSocketToken = :webSocketToken")
    Optional<Auth> findByWebSocketToken(@Param("webSocketToken") String webSocketToken);

    @Query("SELECT a.webSocketToken FROM Auth a WHERE a.user = :user AND a.webSocketToken IS NOT NULL AND a.webSocketToken NOT LIKE :webSocketToken")
    List<String> findAllWebSocketTokensExceptOneByUser(@Param("user") User user, @Param("webSocketToken") String webSocketToken);
}
