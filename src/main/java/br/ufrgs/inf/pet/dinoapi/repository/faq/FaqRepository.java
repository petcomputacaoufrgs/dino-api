package br.ufrgs.inf.pet.dinoapi.repository.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FaqRepository extends CrudRepository<Faq, Long> {

    @Query("SELECT f FROM Faq f JOIN FETCH f.items")
    List<Faq> findAllWithFaqItems();

    @Query("SELECT f FROM Faq f WHERE lower(f.title) = lower(:title)")
    Optional<Faq> findByTitle(@Param("title") String title);
}
