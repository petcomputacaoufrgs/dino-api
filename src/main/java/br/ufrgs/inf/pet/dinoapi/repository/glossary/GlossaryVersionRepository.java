package br.ufrgs.inf.pet.dinoapi.repository.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.glossary.GlossaryVersion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlossaryVersionRepository extends CrudRepository<GlossaryVersion, Long> {

    GlossaryVersion findByOrderByVersionDesc();

}