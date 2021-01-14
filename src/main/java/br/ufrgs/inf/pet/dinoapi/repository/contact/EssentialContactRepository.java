package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EssentialContactRepository extends CrudRepository<EssentialContact, Long> {
    @Query("SELECT ec FROM EssentialContact ec")
    List<EssentialContact> findAll();

    @Query("SELECT ec FROM EssentialContact ec WHERE ec.id IN :ids")
    List<EssentialContact> findAllById(@Param("ids") List<Long> ids);

    @Query("SELECT ec FROM EssentialContact ec WHERE ec.id NOT IN :ids")
    List<EssentialContact> findAllExcludingIds(@Param("ids") List<Long> ids);
}
