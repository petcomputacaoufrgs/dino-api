package br.ufrgs.inf.pet.dinoapi.repository.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaqItemRepository extends CrudRepository<FaqItem, Long> {

    Optional<FaqItem> findByQuestion(String title);

    //List<FaqItem> findAllByExistsTrue();
}
