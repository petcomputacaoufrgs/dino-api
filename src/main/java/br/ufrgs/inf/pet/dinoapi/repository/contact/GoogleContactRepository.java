package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoogleContactRepository extends CrudRepository<GoogleContact, Long> {
    @Query("SELECT n FROM GoogleContact n WHERE n.id = :id AND n.contact.user.id = :userId")
    Optional<GoogleContact> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT n FROM GoogleContact n WHERE n.contact.user.id = :id")
    List<GoogleContact> findAllByUserId(@Param("id") Long id);

    @Query("SELECT n FROM GoogleContact n WHERE n.id IN :ids AND n.contact.user.id = :userId")
    List<GoogleContact> findAllByIdAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Query("SELECT n FROM GoogleContact n WHERE n.id NOT IN :ids AND n.contact.user.id = :userId")
    List<GoogleContact> findAllByUserIdExcludingIds(@Param("userId") Long userId, @Param("ids") List<Long> ids);

    @Query("SELECT n FROM GoogleContact n WHERE n.contact.id = :contactId")
    Optional<GoogleContact> findByContactId(@Param("contactId") Long contactId);

    @Query("SELECT n FROM GoogleContact n WHERE n.user.id = :userId ORDER BY n.contact.id")
    List<GoogleContact> findAllByUserOrderByContactId(@Param("userId") Long contactId);
}
