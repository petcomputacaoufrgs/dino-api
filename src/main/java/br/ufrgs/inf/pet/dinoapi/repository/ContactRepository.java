package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.Note;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {

    @Query("SELECT c FROM Contact c WHERE c.name IN ?1 AND c.user.id = ?2")
    List<Contact> findAllByNameAndUserId(String contactName, Long userId);

    @Query("SELECT c FROM Contact c WHERE c.id IN ?1 AND c.user.id = ?2")
    List<Contact> findAllByIdAndUserId(List<Long> contactIds, Long userId);
}
