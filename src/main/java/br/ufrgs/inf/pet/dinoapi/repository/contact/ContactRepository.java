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

    @Query("SELECT n FROM Contact n WHERE n.id = ?1 AND n.user.id = ?2")
    Optional<Contact> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT n FROM Contact n WHERE n.id IN ?1 AND n.user.id = ?2")
    Optional<List<Contact>> findAllByIdAndUserId(List<Long> ids, Long userId);

}
