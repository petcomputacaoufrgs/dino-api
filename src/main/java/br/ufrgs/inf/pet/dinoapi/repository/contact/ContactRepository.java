package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
    @Query("SELECT n FROM Contact n WHERE n.id = :id AND n.user.id = :userId")
    Optional<Contact> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT n FROM Contact n WHERE n.id IN :ids AND n.user.id = :userId")
    List<Contact> findAllByIdAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Query("SELECT n FROM Contact n WHERE n.id NOT IN :ids AND n.user.id = :userId")
    List<Contact> findAllByUserIdExceptIds(@Param("userId") Long userId, @Param("ids") List<Long> ids);

    @Query("SELECT n FROM Contact n WHERE n.user.id = :userId")
    List<Contact> findAllByUserId(@Param("userId")  Long userId);
    
    @Query("SELECT c FROM Contact c RIGHT JOIN EssentialContactMapping ecm ON ecm.contact.id = c.id WHERE c.user.id = ?1")
    Optional<List<Contact>> findUserEssentialContactsByUserId(Long userId);
    
    @Query("SELECT n FROM Contact n LEFT JOIN FETCH n.googleContacts gc WHERE n.user.id = ?1 AND (gc.user.id = ?1 OR gc.user.id = NULL)")
    List<Contact> findByUserIdWithGoogleContacts(Long userId);
}
