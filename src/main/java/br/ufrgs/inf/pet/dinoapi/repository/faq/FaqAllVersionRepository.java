package br.ufrgs.inf.pet.dinoapi.repository.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqAllVersion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqAllVersionRepository extends CrudRepository<FaqAllVersion, Long> {

    FaqAllVersion findByOrderByVersionDesc();

}
