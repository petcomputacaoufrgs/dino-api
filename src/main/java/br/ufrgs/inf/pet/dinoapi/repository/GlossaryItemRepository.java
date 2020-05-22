package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.GlossaryItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório da entidade: {@link GlossaryItem}
 *
 * @author joao.silva
 */
@Repository
public interface GlossaryItemRepository extends CrudRepository<GlossaryItem, Long> {

    //rep paginado do spring;
    Optional<GlossaryItem> findByTitle(String title);

    List<GlossaryItem> findAllByExistsTrue();

}
