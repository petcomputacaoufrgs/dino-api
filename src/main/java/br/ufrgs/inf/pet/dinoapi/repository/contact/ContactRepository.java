package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.Note;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {

    @Query("SELECT c FROM Contact c WHERE c.name IN ?1 AND c.user.id = ?2")
    List<Contact> findAllByNameAndUserId(String contactName, Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Contact n WHERE n.id = ?1 AND n.user.id = ?2")
    int deleteByIdAndUserId(Long id, Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Contact n WHERE n.id IN ?1 AND n.user.id = ?2")
    int deleteAllByIdAndUserId(List<Long> ids, Long userId);

    @Query("SELECT n FROM Contact n WHERE n.id = ?1 AND n.user.id = ?2")
    Optional<Contact> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT n FROM Contact n WHERE n.id IN ?1 AND n.user.id = ?2")
    Optional<List<Contact>> findAllByIdAndUserId(List<Long> ids, Long userId);

}
