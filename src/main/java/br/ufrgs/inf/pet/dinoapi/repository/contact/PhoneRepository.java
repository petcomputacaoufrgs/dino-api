package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneRepository extends CrudRepository<Phone, Long> {
    @Query("SELECT p FROM Phone p WHERE p.id IN :ids AND p.contact.user.id = :userId")
    List<Phone> findAllByIdAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Query("SELECT p FROM Phone p WHERE p.contact.user.id = :userId AND p.id NOT IN :ids")
    List<Phone> findAllByIdAndUserIdExcludingIds(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Query("SELECT p FROM Phone p WHERE p.contact.user.id = :userId")
    List<Phone> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Phone p WHERE p.contact.id = :contactId")
    List<Phone> findAllByContactId(@Param("contactId") Long contactId);

    @Query("SELECT p.number FROM Phone p WHERE p.contact.id = :contactId")
    List<String> findAllPhoneNumbersByContactId(@Param("contactId") Long contactId);

    @Query("SELECT p FROM Phone p WHERE p.id = :id AND p.contact.user.id = :userId")
    Optional<Phone> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT COUNT(p) FROM Phone p WHERE p.contact.id = :contactId")
    Integer countByContactId(@Param("contactId") Long contactId);
}
