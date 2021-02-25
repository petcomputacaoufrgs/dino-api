package br.ufrgs.inf.pet.dinoapi.repository.treatment;

import br.ufrgs.inf.pet.dinoapi.entity.treatment.TreatmentQuestion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreatmentQuestionRepository extends CrudRepository<TreatmentQuestion, Long> {
    @Query("SELECT n FROM TreatmentQuestion n WHERE n.id = :id AND n.user.id = :userId")
    Optional<TreatmentQuestion> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT n FROM TreatmentQuestion n WHERE n.id IN :ids AND n.user.id = :userId")
    List<TreatmentQuestion> findAllByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Query("SELECT n FROM TreatmentQuestion n WHERE n.id NOT IN :ids AND n.user.id = :userId")
    List<TreatmentQuestion> findAllByUserIdExcludingIds(@Param("userId") Long userId, @Param("ids") List<Long> ids);

    @Query("SELECT n FROM TreatmentQuestion n WHERE n.user.id = :userId")
    List<TreatmentQuestion> findAllByUserId(@Param("userId")  Long userId);
}
