package br.ufrgs.inf.pet.dinoapi.repository.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.glossary.GlossaryItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GlossaryItemRepository extends CrudRepository<GlossaryItem, Long> {

    Optional<GlossaryItem> findByTitle(String title);

    List<GlossaryItem> findAllByExistsTrue();

}
