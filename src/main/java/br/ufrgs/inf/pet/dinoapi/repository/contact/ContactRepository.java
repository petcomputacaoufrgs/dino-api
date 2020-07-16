package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {

    @Query("SELECT c FROM Contact c WHERE c.name IN ?1 AND c.user.id = ?2")
    List<Contact> findAllByNameAndUserId(String contactName, Long userId);

    Optional<Contact> findByName(String contactName);


}
