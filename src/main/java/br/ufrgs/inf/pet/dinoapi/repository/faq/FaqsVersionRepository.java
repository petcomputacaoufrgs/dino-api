package br.ufrgs.inf.pet.dinoapi.repository.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqsVersion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqsVersionRepository extends CrudRepository<FaqsVersion, Long> {

    FaqsVersion findByOrderByVersionDesc();

}
