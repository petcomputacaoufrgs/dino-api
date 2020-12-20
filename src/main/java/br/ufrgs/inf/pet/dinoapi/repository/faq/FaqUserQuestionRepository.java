package br.ufrgs.inf.pet.dinoapi.repository.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqUserQuestion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FaqUserQuestionRepository extends CrudRepository<FaqUserQuestion, Long> {
    @Query("SELECT n FROM FaqUserQuestion n WHERE n.id = :id AND n.user.id = :userId")
    Optional<FaqUserQuestion> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT n FROM FaqUserQuestion n WHERE n.id IN :ids AND n.user.id = :userId")
    List<FaqUserQuestion> findAllByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Query("SELECT n FROM FaqUserQuestion n WHERE n.id NOT IN :ids AND n.user.id = :userId")
    List<FaqUserQuestion> findAllByUserIdExceptIds(@Param("userId") Long userId, @Param("ids") List<Long> ids);

    @Query("SELECT n FROM FaqUserQuestion n WHERE n.user.id = :userId")
    List<FaqUserQuestion> findAllByUserId(@Param("userId")  Long userId);
}
