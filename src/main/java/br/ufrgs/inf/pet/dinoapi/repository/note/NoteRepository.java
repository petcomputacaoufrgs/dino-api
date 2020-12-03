package br.ufrgs.inf.pet.dinoapi.repository.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

        @Query("SELECT n FROM Note n WHERE n.id = :id AND n.noteColumn.user.id = :userId")
        Optional<Note> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userID);

        @Query("SELECT n FROM Note n LEFT JOIN FETCH n.tags WHERE n.noteColumn.user.id = :userId")
        List<Note> findAllByUserIdWithTags(@Param("userId") Long userID);

        @Query("SELECT n FROM Note n WHERE (n.id IN :ids) AND n.noteColumn.user.id = :userId")
        List<Note> findAllByIdAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userID);

        @Query("SELECT n FROM Note n WHERE n.question = :question AND n.noteColumn.user.id = :userId")
        Optional<Note> findByQuestionAndUserId(@Param("question") String question, @Param("userId") Long userId);

        @Query("SELECT n FROM Note n WHERE n.id IN :ids AND n.noteColumn.user.id = :userId ORDER BY n.id ASC")
        List<Note> findAllByIdOrderByIdAsc(@Param("ids") List<Long> ids, @Param("userId") Long userId);

        @Query("SELECT MAX(n.order) FROM Note n WHERE n.noteColumn.id = :columnId AND n.noteColumn.user.id = :userId")
        Optional<Integer> findMaxOrderByUserIdAndColumnId(@Param("userId") Long userId, @Param("columnId") Long columnId);

        @Query("SELECT COUNT(n.id) FROM Note n WHERE n.noteColumn.id = :id")
        Integer countNotesByNoteColumnId(@Param("id") Long id);

        @Query("SELECT COUNT(n.id) FROM Note n WHERE n.noteColumn.id IN :ids")
        Integer countNotesByNoteColumnsIds(@Param("ids") List<Long> ids);

        @Query("SELECT n.question FROM Note n WHERE n.noteColumn.user.id = :userId")
        List<String> findAllQuestionsByUserId(@Param("userId") Long userId);

        @Query("SELECT COUNT(n) FROM Note n WHERE n.noteColumn.id = :noteColumnId")
        Long countNotesByColumnId(@Param("noteColumnId") Long noteColumnId);

}
