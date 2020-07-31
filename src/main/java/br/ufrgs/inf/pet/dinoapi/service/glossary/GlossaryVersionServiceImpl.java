package br.ufrgs.inf.pet.dinoapi.service.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.GlossaryVersion;
import br.ufrgs.inf.pet.dinoapi.repository.GlossaryVersionRepository;
import br.ufrgs.inf.pet.dinoapi.websocket.service.glossary.GlossaryWebSocketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GlossaryVersionServiceImpl implements GlossaryVersionService {

    @Autowired
    GlossaryVersionRepository glossaryVersionRepository;

    @Autowired
    GlossaryWebSocketServiceImpl glossaryWebSocketService;

    @Override
    public Long updateGlossaryVersion() {
        GlossaryVersion glossary = glossaryVersionRepository.findByOrderByVersionDesc();
        if (glossary != null) {
            glossary.updateVersion();
        } else {
            glossary = createFirstGlossaryVersion();
        }

        glossaryVersionRepository.save(glossary);
        glossaryWebSocketService.sendGlossaryUpdateMessage(glossary.getVersion());


        return glossary.getVersion();
    }

    @Override
    public ResponseEntity<Long> getGlossaryVersion() {
        GlossaryVersion glossary = glossaryVersionRepository.findByOrderByVersionDesc();

        if (glossary == null) {
            glossary = createFirstGlossaryVersion();
            glossaryVersionRepository.save(glossary);
        }

        return new ResponseEntity<>(glossary.getVersion(), HttpStatus.OK);
    }

    @Override
    public Long getGlossaryVersionNumber() {
        GlossaryVersion glossary = glossaryVersionRepository.findByOrderByVersionDesc();

        if (glossary == null) {
            glossary = createFirstGlossaryVersion();
            glossaryVersionRepository.save(glossary);
        }

        return glossary.getVersion();
    }


    private GlossaryVersion createFirstGlossaryVersion() {
        GlossaryVersion glossary = new GlossaryVersion();
        glossary.setLastUpdate(new Date());
        glossary.setVersion(0l);

        return glossary;
    }
}
