package br.ufrgs.inf.pet.dinoapi.repository.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {
        @Query("SELECT n FROM Note n WHERE n.id = :id AND n.noteColumn.user.id = :userId")
        Optional<Note> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userID);

        @Query("SELECT n FROM Note n WHERE n.noteColumn.user.id = :userId")
        List<Note> findAllByUserId(@Param("userId") Long userId);

        @Query("SELECT n FROM Note n WHERE (n.id IN :ids) AND n.noteColumn.user.id = :userId")
        List<Note> findAllByIdAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userID);

        @Query("SELECT COUNT(n) FROM Note n WHERE n.noteColumn.id = :columnId AND n.lastUpdate >= :lastUpdate")
        Integer countByNoteColumnAndLastUpdateGreaterOrEqual(@Param("columnId") Long columnId, @Param("lastUpdate") LocalDateTime lastUpdate);
}
