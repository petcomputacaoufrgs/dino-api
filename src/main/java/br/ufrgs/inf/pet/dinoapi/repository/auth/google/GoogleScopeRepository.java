package br.ufrgs.inf.pet.dinoapi.repository.auth.google;

import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleScope;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoogleScopeRepository extends CrudRepository<GoogleScope, Long>  {
    @Query("SELECT n FROM GoogleScope n WHERE n.id = :id AND n.googleAuth.user.id = :userId")
    Optional<GoogleScope> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT n FROM GoogleScope n WHERE n.id IN :ids AND n.googleAuth.user.id = :userId")
    List<GoogleScope> findAllByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Query("SELECT n FROM GoogleScope n WHERE n.id NOT IN :ids AND n.googleAuth.user.id = :userId")
    List<GoogleScope> findAllByUserIdExcludingIds(@Param("userId") Long userId, @Param("ids") List<Long> ids);

    @Query("SELECT n FROM GoogleScope n WHERE n.googleAuth.user.id = :userId")
    List<GoogleScope> findAllByUserId(@Param("userId")  Long userId);

    @Query("SELECT n FROM GoogleScope n WHERE n.googleAuth.user.id = :userId AND n.name IN :names")
    List<GoogleScope> findAllByName(@Param("userId") Long userId, @Param("names") List<String> names);

    @Query("SELECT n FROM GoogleScope n WHERE n.googleAuth.user.id = :userId AND n.name = :name")
    Optional<GoogleScope> findByName(@Param("userId") Long userId, @Param("name") String name);
}
