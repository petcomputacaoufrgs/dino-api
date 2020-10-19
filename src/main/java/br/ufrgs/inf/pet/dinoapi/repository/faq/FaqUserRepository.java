package br.ufrgs.inf.pet.dinoapi.repository.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FaqUserRepository extends CrudRepository<FaqUser, Long> {

    @Query("SELECT fu FROM FaqUser fu WHERE fu.user.id = :userId")
    Optional<FaqUser> findByUserId(@Param("userId") Long userID);
}
