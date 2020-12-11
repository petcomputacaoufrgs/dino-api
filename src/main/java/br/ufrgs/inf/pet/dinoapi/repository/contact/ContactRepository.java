package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {

    @Query("SELECT n FROM Contact n WHERE n.id = ?1 AND n.user.id = ?2")
    Optional<Contact> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT n FROM Contact n WHERE n.id IN ?1 AND n.user.id = ?2")
    List<Contact> findAllByIdAndUserId(List<Long> ids, Long userId);

    @Query("SELECT n FROM Contact n WHERE n.user.id = ?1")
    List<Contact> findAllByUserId(Long userId);

    @Query("SELECT n FROM Contact n LEFT JOIN FETCH n.googleContacts gc WHERE n.user.id = ?1 AND (gc.user.id = ?1 OR gc.user.id = NULL)")
    List<Contact> findByUserIdWithGoogleContacts(Long userId);

}
