package br.ufrgs.inf.pet.dinoapi.repository.auth.google;

import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleScope;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoogleScopeRepository extends CrudRepository<GoogleScope, Long>  {
    @Query("SELECT gs.name FROM GoogleScope gs WHERE gs.googleAuth.id = :googleAuthId")
    List<String> findAllNamesByGoogleAuthId(@Param("googleAuthId") Long googleAuthId);
}
