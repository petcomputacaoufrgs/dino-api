package br.ufrgs.inf.pet.dinoapi.repository.treatment;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TreatmentRepository extends CrudRepository<Treatment, Long> {
    @Query("SELECT te FROM Treatment te")
    List<Treatment> findAll();

    @Query("SELECT te FROM Treatment te WHERE te.id IN :ids")
    List<Treatment> findByIds(@Param("ids") List<Long> ids);

    @Query("SELECT te FROM Treatment te WHERE te.id IN :ids")
    List<Treatment> findAllByIds(@Param("ids") List<Long> ids);

    @Query("SELECT te FROM Treatment te WHERE te.id IN :ids")
    List<Treatment> findAllExcludingIds(@Param("ids") List<Long> ids);

    @Query("SELECT te FROM Treatment te WHERE :essentialContact IN (te.essentialContacts) ")
    List<Treatment> findAllByEssentialContact(@Param("essentialContact") EssentialContact essentialContact);
}
