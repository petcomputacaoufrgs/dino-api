package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EssentialContactRepository extends CrudRepository<EssentialContact, Long> {

    List<EssentialContact> findByFaqIdIsNull();

}
