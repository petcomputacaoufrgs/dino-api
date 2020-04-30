package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.Note;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

        @Query("SELECT n FROM Note n WHERE n.id IN :ids ORDER BY n.id ASC")
        List<Note> findAllByIdOrderByIdAsc(List<Long> ids);

}
