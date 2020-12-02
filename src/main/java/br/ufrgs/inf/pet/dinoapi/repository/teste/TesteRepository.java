package br.ufrgs.inf.pet.dinoapi.repository.teste;

import br.ufrgs.inf.pet.dinoapi.entity.teste.TesteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TesteRepository extends CrudRepository<TesteEntity, Long> {
    @Query("SELECT t FROM TesteEntity t WHERE t.id = :id AND t.user.id = :userId")
    Optional<TesteEntity> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userID);

    @Query("SELECT t FROM TesteEntity t WHERE t.user.id = :userId")
    List<TesteEntity> findByUserId(@Param("userId") Long userID);

    @Query("SELECT t FROM TesteEntity t WHERE t.id IN :ids AND t.user.id = :userId")
    List<TesteEntity> findByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userID);
}
