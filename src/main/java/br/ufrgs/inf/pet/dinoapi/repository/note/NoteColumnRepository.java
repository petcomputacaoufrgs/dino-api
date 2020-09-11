package br.ufrgs.inf.pet.dinoapi.repository.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteColumnRepository extends CrudRepository<NoteColumn, Long> {
    @Query("SELECT n FROM NoteColumn n WHERE n.id = :id AND n.user.id = :userId")
    Optional<NoteColumn> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userID);

    @Query("SELECT n FROM NoteColumn n WHERE (n.id IN :ids) AND n.user.id = :userId")
    List<NoteColumn> findAllByIdAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userID);

    @Query("SELECT n FROM NoteColumn n WHERE n.title = :title AND n.user.id = :userId")
    Optional<NoteColumn> findByTitleAndUserId(@Param("title") String title, @Param("userId") Long userId);

    @Query("SELECT n FROM NoteColumn n WHERE n.title IN :titles AND n.user.id = :userId")
    List<NoteColumn> findByTitlesAndUserId(@Param("titles") List<String> titles, @Param("userId") Long userId);

    @Query("SELECT MAX(n.order) FROM NoteColumn n WHERE n.user.id = :userId")
    Optional<Integer> findMaxOrderByUserId(@Param("userId") Long userId);

    @Query("SELECT n FROM NoteColumn n WHERE n.id IN :ids AND n.user.id = :userId ORDER BY n.id ASC")
    List<NoteColumn> findAllByIdOrderByIdAsc(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM NoteColumn n WHERE n.id = :id AND n.user.id = :userId AND n.notes.size = 0")
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM NoteColumn n WHERE n.id IN :ids AND n.user.id = :userId AND n.notes.size = 0")
    int deleteAllByIdAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

}
