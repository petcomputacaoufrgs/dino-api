package br.ufrgs.inf.pet.dinoapi.repository.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FaqItemRepository extends CrudRepository<FaqItem, Long> {
    @Query("SELECT fi FROM FaqItem fi")
    List<FaqItem> findAll();

    @Query("SELECT fi FROM FaqItem fi WHERE fi.id IN :ids")
    List<FaqItem> findAllByIds(@Param("ids") List<Long> ids);

    @Query("SELECT fi FROM FaqItem fi WHERE fi.id NOT IN :ids")
    List<FaqItem> findAllExcludingIds(@Param("ids") List<Long> ids);
}
