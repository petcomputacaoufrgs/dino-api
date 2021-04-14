package br.ufrgs.inf.pet.dinoapi.repository.auth;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.projection.auth.AuthWebSocketToken;
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

    @Query("SELECT a FROM Auth a WHERE a.refreshToken = :refreshToken")
    Optional<Auth> findByRefreshToken(@Param("refreshToken") String refreshToken);

    @Query("SELECT a FROM Auth a WHERE a.webSocketToken = :webSocketToken")
    Optional<Auth> findByWebSocketToken(@Param("webSocketToken") String webSocketToken);

    @Query("SELECT a.webSocketToken as webSocketToken FROM Auth a WHERE a.user = :user AND a.webSocketToken IS NOT NULL AND a.webSocketToken NOT LIKE :webSocketToken")
    List<AuthWebSocketToken> findAllByUserExceptWithThisWebSocketToken(@Param("user") User user, @Param("webSocketToken") String webSocketToken);

    @Query("SELECT a.webSocketToken as webSocketToken FROM Auth a WHERE a.user = :user AND a.webSocketToken IS NOT NULL")
    List<AuthWebSocketToken> findAllByUser(@Param("user") User user);

    @Query("SELECT a.webSocketToken as webSocketToken FROM Auth a WHERE a.webSocketToken IS NOT NULL AND a.webSocketToken NOT LIKE :webSocketToken")
    List<AuthWebSocketToken> findAllExceptOneWebSocketToken(@Param("webSocketToken") String webSocketToken);

    @Query("SELECT a.webSocketToken as webSocketToken FROM Auth a WHERE a.webSocketToken IS NOT NULL")
    List<AuthWebSocketToken> findAllTokens();
}
