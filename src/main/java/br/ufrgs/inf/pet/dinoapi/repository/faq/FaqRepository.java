package br.ufrgs.inf.pet.dinoapi.repository.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FaqRepository extends CrudRepository<Faq, Long> {
    @Query("SELECT f FROM Faq f")
    List<Faq> findAll();

    @Query("SELECT f FROM Faq f WHERE f.id IN :ids")
    List<Faq> findAllByIds(@Param("ids") List<Long> ids);

    @Query("SELECT f FROM Faq f WHERE f.id NOT IN :ids")
    List<Faq> findAllExcludingIds(@Param("ids") List<Long> ids);
}
