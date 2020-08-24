package br.ufrgs.inf.pet.dinoapi.repository.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FaqItemRepository extends CrudRepository<FaqItem, Long> {

    Optional<FaqItem> findByQuestion(String title);

    @Transactional
    @Modifying
    @Query("DELETE FROM FaqItem i WHERE i.id IN :ids")
    int deleteAllById(List<Long> ids);
}
