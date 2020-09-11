package br.ufrgs.inf.pet.dinoapi.service.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.glossary.GlossaryItem;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossarySaveRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryUpdateRequestModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GlossaryService {

    ResponseEntity<GlossaryResponseModel> save(GlossarySaveRequestModel glossarySaveRequestModel);

    ResponseEntity<?> update(GlossaryUpdateRequestModel glossaryUpdateRequestModel);

    ResponseEntity<List<GlossaryItem>> get();
}
