package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.GlossaryItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório da entidade: {@link GlossaryItem}
 *
 * @author joao.silva
 */
@Repository
public interface GlossaryItemRepository extends CrudRepository<GlossaryItem, Long> {

    GlossaryItem findOneById(Long id);

    GlossaryItem findOneByTitle(String title);

    List<GlossaryItem> findAllByExistsTrue();

}
