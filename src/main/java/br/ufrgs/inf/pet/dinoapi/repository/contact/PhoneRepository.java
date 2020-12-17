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
    List<Phone> findAllByIdAndContactUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Query("SELECT p FROM Phone p WHERE p.contact.user.id = :userId AND p.id NOT IN :ids")
    List<Phone> findAllByContactUserIdExceptIds(@Param("userId") Long userId, @Param("ids") List<Long> ids);

    List<Phone> findAllByContactUserId(Long userId);

    Optional<Phone> findByIdAndContactUserId(Long id, Long id1);
}
