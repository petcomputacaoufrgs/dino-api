package br.ufrgs.inf.pet.dinoapi.repository.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteColumnRepository extends CrudRepository<NoteColumn, Long> {
    @Query("SELECT n FROM NoteColumn n WHERE n.id = :id AND n.user.id = :userId")
    Optional<NoteColumn> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userID);

    @Query("SELECT n FROM NoteColumn n WHERE n.user.id = :userId")
    List<NoteColumn> findAllByUserId(@Param("userId") Long userID);

    @Query("SELECT n FROM NoteColumn n WHERE n.id IN :ids AND n.user.id = :userId")
    List<NoteColumn> findAllByIdAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userID);

    @Query("SELECT n FROM NoteColumn n WHERE n.id NOT IN :ids AND n.user.id = :userId")
    List<NoteColumn> findAllByUserIdExcludingIds(@Param("userId") Long userID, @Param("ids") List<Long> ids);
}
