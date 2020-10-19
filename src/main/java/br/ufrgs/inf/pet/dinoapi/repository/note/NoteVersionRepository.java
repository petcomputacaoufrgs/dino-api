package br.ufrgs.inf.pet.dinoapi.repository.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteVersion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NoteVersionRepository extends CrudRepository<NoteVersion, Long> {
    @Query("SELECT nv FROM NoteVersion nv WHERE nv.user.id = :userId")
    Optional<NoteVersion> findByUserId(@Param("userId") Long userId);
}

