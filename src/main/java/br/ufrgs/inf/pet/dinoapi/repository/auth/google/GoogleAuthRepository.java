package br.ufrgs.inf.pet.dinoapi.repository.auth.google;


import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoogleAuthRepository extends CrudRepository<GoogleAuth, Long>  {

    @Query("SELECT g FROM GoogleAuth g WHERE g.googleId = :googleId")
    Optional<GoogleAuth> findByGoogleId(String googleId);

}
