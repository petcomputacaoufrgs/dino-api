package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialPhone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Query("SELECT c FROM Contact c WHERE c.essentialContact.id = :essentialContactId")
    List<Contact> findAllByEssentialContactId(Long essentialContactId);

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId ORDER BY c.id")
    List<Contact> findAllByUserOrderById(Long userId);

    @Query("SELECT c FROM Contact c " +
            "WHERE c.essentialContact = :essentialContact " +
            "AND (SELECT COUNT(p) FROM Phone p WHERE p.essentialPhone = :essentialPhone) = 0")
    List<Contact> findAllWhichShouldHaveEssentialPhoneButDoesnt(EssentialContact essentialContact,
                                                                EssentialPhone essentialPhone);
}
