package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EssentialContactRepository extends CrudRepository<EssentialContact, Long> {

    List<EssentialContact> findByFaqIdIsNull();

    @Query("SELECT e FROM EssentialContact e WHERE e.contact.name = ?1")
    Optional<EssentialContact> findByEssentialContactName(String name);

    @Query("SELECT e FROM EssentialContact e WHERE e.contact.name = ?1 AND e.faq.id = ?2")
    Optional<EssentialContact> findByEssentialContactNameAndFaqId(String name, Long id);

    @Query("SELECT e FROM EssentialContact e WHERE e.faq.id = ?1")
    Optional<List<EssentialContact>> findEssentialContactsByFaqId(Long id);

}
