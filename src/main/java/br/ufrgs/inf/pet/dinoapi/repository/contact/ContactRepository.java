package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
    @Query("SELECT c FROM Contact c WHERE c.id = :id AND c.user.id = :userId")
    Optional<Contact> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT c FROM Contact c WHERE c.id IN :ids AND c.user.id = :userId")
    List<Contact> findAllByIdsAndUserId(List<Long> ids, Long userId);

    @Query("SELECT c FROM Contact c WHERE c.id NOT IN :ids AND c.user.id = :userId")
    List<Contact> findAllByUserIdExcludingIds(Long userId, List<Long> ids);

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findAllByUserId(Long userId);

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId ORDER BY c.id")
    List<Contact> findAllByUserOrderById(Long userId);
}
