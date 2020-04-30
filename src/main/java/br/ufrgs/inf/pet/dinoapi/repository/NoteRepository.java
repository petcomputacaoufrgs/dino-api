package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.Note;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

        @Query("SELECT n FROM Note n WHERE n.question = ?1 AND n.user.id = ?2")
        List<Note> findByQuestionAndUserId(String question, Long userId);

        @Query("SELECT n FROM Note n WHERE n.id IN :ids ORDER BY n.id ASC")
        List<Note> findAllByIdOrderByIdAsc(List<Long> ids);

        @Transactional
        @Modifying
        @Query("DELETE FROM Note n WHERE n.id = ?1 AND n.user.id = ?2")
        int deleteByIdAndUserId(Long id, Long userId);

}
