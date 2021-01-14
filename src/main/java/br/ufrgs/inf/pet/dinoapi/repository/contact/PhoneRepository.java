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

    @Query("SELECT p FROM Phone p WHERE p.essentialContact IS NOT NULL AND p.id NOT IN :ids")
    List<Phone> findAllEssentialPhonesExcludingIds(@Param("ids") List<Long> ids);

    @Query("SELECT p FROM Phone p WHERE p.contact.user.id = :userId")
    List<Phone> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Phone p WHERE p.essentialContact IS NOT NULL")
    List<Phone> findAllEssentialPhones();

    @Query("SELECT p FROM Phone p WHERE p.essentialContact.id = :essentialContactId")
    List<Phone> findAllByEssentialContactId(@Param("essentialContactId") Long essentialContactId);

    @Query("SELECT p FROM Phone p WHERE p.originalEssentialPhone = :originalEssentialPhone")
    List<Phone> findAllByOriginalEssentialPhone(@Param("originalEssentialPhone") Phone originalEssentialPhone);

    @Query("SELECT p FROM Phone p WHERE p.contact.id = :contactId")
    List<Phone> findAllByContactId(@Param("contactId") Long contactId);

    @Query("SELECT p FROM Phone p WHERE p.id = :id AND p.contact.user.id = :userId")
    Optional<Phone> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT p FROM Phone p WHERE p.id = :id AND p.essentialContact IS NOT NULL")
    Optional<Phone> findEssentialById(@Param("id") Long id);
}
