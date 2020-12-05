package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContactMapping;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EssentialContactMappingRepository extends CrudRepository<EssentialContactMapping, Long> {

    @Query("SELECT ecm FROM EssentialContactMapping ecm LEFT JOIN Contact c ON ecm.contact.id = c.id WHERE c.user.id = ?1")
    Optional<List<EssentialContactMapping>> findEssentialContactsByUserId(Long userId);

}
