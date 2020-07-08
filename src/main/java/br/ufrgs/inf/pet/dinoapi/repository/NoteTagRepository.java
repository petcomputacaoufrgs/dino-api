package br.ufrgs.inf.pet.dinoapi.repository;

import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteTagRepository extends CrudRepository<NoteTag, Long> {

    @Query("SELECT nt FROM NoteTag nt WHERE nt.id IN :ids")
    List<NoteTag> findAllById(@Param("ids") Iterable<Long> ids);

    @Query("SELECT n.tags FROM Note n WHERE n.id IN :ids")
    List<NoteTag> findAllByNotes(@Param("ids") Iterable<Long> ids);

    @Query("SELECT t FROM NoteTag t WHERE t.name IN :names")
    List<NoteTag> findAllByName(@Param("names") Iterable<String> names);

}
