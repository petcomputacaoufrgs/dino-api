package br.ufrgs.inf.pet.dinoapi.repository.google;

import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAccessToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GoogleAccessTokenRepository extends CrudRepository<GoogleAccessToken, Long> {
    @Query("SELECT gat FROM GoogleAccessToken gat WHERE gat.googleAuth.user.id = :userId ")
    Optional<GoogleAccessToken> findByUserId(@Param("userId") Long userId);
}
