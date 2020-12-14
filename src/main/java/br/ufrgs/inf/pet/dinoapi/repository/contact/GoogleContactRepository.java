package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoogleContactRepository extends CrudRepository<GoogleContact, Long> {
    @Query("SELECT n FROM GoogleContact n WHERE n.contact.id = ?1 AND n.contact.user.id = ?2")
    Optional<GoogleContact> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT n FROM GoogleContact n WHERE n.contact.user.id = ?1")
    List<GoogleContact> findAllByUserId(Long id);

    @Query("SELECT n FROM GoogleContact n WHERE n.contact.id IN ?1 AND n.contact.user.id = ?2")
    List<GoogleContact> findAllByIdAndUserId(List<Long> ids, Long userId);
}
