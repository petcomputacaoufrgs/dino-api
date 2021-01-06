package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EssentialContactRepository extends CrudRepository<EssentialContact, Long> {

    @Query("SELECT e FROM EssentialContact e WHERE e.contact.id IN :ids")
    List<EssentialContact> findByIds(@Param("ids") List<Long> ids);

}
