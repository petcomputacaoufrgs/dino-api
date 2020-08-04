package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.GlossaryVersion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlossaryVersionRepository extends CrudRepository<GlossaryVersion, Long> {

    GlossaryVersion findByOrderByVersionDesc();

}