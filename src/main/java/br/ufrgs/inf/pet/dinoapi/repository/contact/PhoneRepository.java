package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneRepository extends CrudRepository<Phone, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Phone p WHERE p.id IN :ids")
    int deleteAllById(@Param("ids") List<Long> ids);

    @Query("SELECT p FROM Phone p WHERE p.contact.id = :contactId")
    List<Phone> getPhonesByContactId(@Param("contactId") Long contactId);

    List<Phone> findAllByIdAndUserId(List<Long> ids, Long id);

    List<Phone> findAllByUserId(Long id);

    Optional<Phone> findByIdAndUserId(Long id, Long id1);
}
