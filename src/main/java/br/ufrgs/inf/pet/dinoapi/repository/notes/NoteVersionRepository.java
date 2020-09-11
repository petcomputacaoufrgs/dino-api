package br.ufrgs.inf.pet.dinoapi.repository.notes;

import br.ufrgs.inf.pet.dinoapi.entity.notes.NoteVersion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteVersionRepository extends CrudRepository<NoteVersion, Long> {
}

