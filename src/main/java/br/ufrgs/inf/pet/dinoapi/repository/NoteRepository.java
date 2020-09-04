package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.Note;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

        @Query("SELECT n FROM Note n WHERE (n.id IN :ids) AND n.user.id = :userId")
        List<Note> findAllByIdAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userID);

        @Query("SELECT n FROM Note n WHERE n.question = :question AND n.user.id = :userId")
        List<Note> findByQuestionAndUserId(@Param("question") String question, @Param("userId") Long userId);


        @Query("SELECT n FROM Note n WHERE n.id IN :ids AND n.user.id = :userId ORDER BY n.id ASC")
        List<Note> findAllByIdOrderByIdAsc(@Param("ids") List<Long> ids, @Param("userId") Long userId);

        @Transactional
        @Modifying
        @Query("DELETE FROM Note n WHERE n.id IN :ids AND n.user.id = :userId")
        int deleteAllByIdAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

        @Transactional
        @Modifying
        @Query("DELETE FROM Note n WHERE n.id = :id AND n.user.id = :userId")
        int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

        @Query("SELECT MAX(n.order) FROM Note n WHERE n.user.id = :userId")
        Optional<Integer> findMaxOrderByUserId(@Param("userId") Long userId);
}
