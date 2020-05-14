package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.Note;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

        @Query("SELECT n FROM Note n WHERE n.id IN ?1 AND n.user.id = ?2")
        List<Note> findAllByIdAndUserId(List<Long> ids, Long userID);

        @Query("SELECT n FROM Note n WHERE n.question = ?1 AND n.user.id = ?2")
        List<Note> findByQuestionAndUserId(String question, Long userId);

        @Query("SELECT n FROM Note n WHERE n.id IN ?1 AND n.user.id = ?2 ORDER BY n.id ASC")
        List<Note> findAllByIdOrderByIdAsc(List<Long> ids, Long userId);

        @Transactional
        @Modifying
        @Query("DELETE FROM Note n WHERE n.id IN ?1 AND n.user.id = ?2")
        int deleteAllByIdAndUserId(List<Long> ids, Long userId);

        @Transactional
        @Modifying
        @Query("DELETE FROM Note n WHERE n.id = ?1 AND n.user.id = ?2")
        int deleteByIdAndUserId(Long id, Long userId);

        @Query("SELECT n FROM Note n WHERE n.id = ?1 AND n.user.id = ?2")
        Optional<Note> findOneByIdAndUserId(Long id, Long userId);

        @Query("SELECT MAX(n.order) FROM Note n WHERE n.user.id = ?1")
        Optional<Integer> findMaxOrderByUserId(Long userId);

}
