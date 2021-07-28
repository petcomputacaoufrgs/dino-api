package br.ufrgs.inf.pet.dinoapi.repository.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialPhone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EssentialPhoneRepository extends CrudRepository<EssentialPhone, Long> {
    @Query("SELECT ep FROM EssentialPhone ep")
    List<EssentialPhone> findAll();

    @Query("SELECT ep FROM EssentialPhone ep WHERE ep.id IN :ids")
    List<EssentialPhone> findAllById(@Param("ids") List<Long> ids);

    @Query("SELECT ep FROM EssentialPhone ep WHERE ep.id NOT IN :ids")
    List<EssentialPhone> findAllExcludingIds(@Param("ids") List<Long> ids);
}
