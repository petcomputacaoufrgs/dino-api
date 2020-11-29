package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GoogleContactRepository extends CrudRepository<GoogleContact, Long> {
    @Query("SELECT n FROM GoogleContact n WHERE n.contact.id = ?1 AND n.user.id = ?2")
    Optional<GoogleContact> findByContactIdAndUserId(Long contactId, Long userId);
}
