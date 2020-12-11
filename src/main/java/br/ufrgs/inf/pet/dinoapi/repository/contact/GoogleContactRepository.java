package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoogleContactRepository extends CrudRepository<GoogleContact, Long> {
    @Query("SELECT n FROM GoogleContact n WHERE n.contact.id = ?1 AND n.user.id = ?2")
    Optional<GoogleContact> findByContactIdAndUserId(Long contactId, Long userId);

    Optional<GoogleContact> findByIdAndUserId(Long id, Long id1);

    List<GoogleContact> findAllByUserId(Long id);

    List<GoogleContact> findAllByIdAndUserId(List<Long> ids, Long id);
}
