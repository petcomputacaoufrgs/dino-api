package br.ufrgs.inf.pet.dinoapi.repository.auth;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Staff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends CrudRepository<Staff, Long> {

    @Query("SELECT s FROM Staff s WHERE s.id NOT IN :ids")
    List<Staff> findAllExcludingIds(@Param("ids") List<Long> ids);
}
