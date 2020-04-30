package br.ufrgs.inf.pet.dinoapi.service.glossary;

import org.springframework.http.ResponseEntity;

public interface GlossaryVersionService {

    Long updateGlossaryVersion();

    ResponseEntity<Long> getGlossaryVersion();

    Long getGlossaryVersionNumber();
}
