package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneRepository extends CrudRepository<Phone, Long> {

    @Query("SELECT p FROM Phone p WHERE p.id IN ?1 AND p.contact.user.id = ?2")
    List<Phone> findAllByIdAndContactUserId(List<Long> ids, Long userId);

    List<Phone> findAllByContactUserId(Long userId);

    Optional<Phone> findByIdAndContactUserId(Long id, Long id1);
}
