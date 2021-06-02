package br.ufrgs.inf.pet.dinoapi.repository.kids_space;

import br.ufrgs.inf.pet.dinoapi.entity.kids_space.KidsSpaceSettings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KidsSpaceSettingsRepository extends CrudRepository<KidsSpaceSettings, Long> {
    @Query("SELECT ks FROM KidsSpaceSettings ks WHERE ks.id = :id AND ks.user.id = :userId")
    Optional<KidsSpaceSettings> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT ks FROM KidsSpaceSettings ks WHERE ks.user.id = :userId")
    List<KidsSpaceSettings> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT ks FROM KidsSpaceSettings ks WHERE ks.id IN :ids AND ks.user.id = :userId")
    List<KidsSpaceSettings> findAllByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Query("SELECT ks FROM KidsSpaceSettings ks WHERE ks.id NOT IN :ids AND ks.user.id = :userId")
    List<KidsSpaceSettings> findAllByUserIdExcludingIds(@Param("userId") Long userId, @Param("ids") List<Long> ids);
}
