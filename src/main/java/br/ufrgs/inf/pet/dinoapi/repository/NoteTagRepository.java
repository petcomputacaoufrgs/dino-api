package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteTagRepository extends CrudRepository<NoteTag, Long> {

    @Query("SELECT nt FROM NoteTag nt WHERE nt.id IN :ids")
    List<NoteTag> findAllById(Iterable<Long> ids);

    @Query("SELECT n.tags FROM Note n WHERE n.id IN :ids")
    List<NoteTag> findAllByNotes(Iterable<Long> ids);

    @Query("SELECT n.tags FROM Note n WHERE n.user.id = ?1")
    List<NoteTag> findAllByUserId(Long userId);

}
