package br.ufrgs.inf.pet.dinoapi.repository.auth.google;

import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleScope;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GoogleScopeRepository extends CrudRepository<GoogleScope, Long>  {
    @Transactional
    @Modifying
    @Query("DELETE FROM GoogleScope gs WHERE gs.googleAuth.id = :googleAuthId")
    void deleteAllByGoogleAuthId(@Param("googleAuthId") Long googleAuthId);
}
