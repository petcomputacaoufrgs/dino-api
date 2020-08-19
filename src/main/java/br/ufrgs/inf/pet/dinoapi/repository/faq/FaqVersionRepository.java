package br.ufrgs.inf.pet.dinoapi.repository.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqVersion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqVersionRepository extends CrudRepository<FaqVersion, Long> {

    @Query("SELECT v FROM FaqVersion v WHERE v.faq.id = ?1 ORDER BY v.id DESC")
    FaqVersion findVersionDescById(Long faqId);

}
