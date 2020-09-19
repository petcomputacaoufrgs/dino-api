package br.ufrgs.inf.pet.dinoapi.service.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.glossary.GlossaryVersion;
import br.ufrgs.inf.pet.dinoapi.repository.glossary.GlossaryVersionRepository;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.topic.AlertUpdateTopicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GlossaryVersionServiceImpl implements GlossaryVersionService {

    private final GlossaryVersionRepository glossaryVersionRepository;

    private final AlertUpdateTopicServiceImpl alertUpdateTopicService;

    @Autowired
    public GlossaryVersionServiceImpl(GlossaryVersionRepository glossaryVersionRepository, AlertUpdateTopicServiceImpl alertUpdateTopicService) {
        this.glossaryVersionRepository = glossaryVersionRepository;
        this.alertUpdateTopicService = alertUpdateTopicService;
    }

    @Override
    public Long updateGlossaryVersion() {
        GlossaryVersion glossary = glossaryVersionRepository.findByOrderByVersionDesc();
        if (glossary != null) {
            glossary.updateVersion();
        } else {
            glossary = new GlossaryVersion();
        }

        glossaryVersionRepository.save(glossary);
        alertUpdateTopicService.sendUpdateMessage(glossary.getVersion(), WebSocketDestinationsEnum.ALERT_GLOSSARY_UPDATE);


        return glossary.getVersion();
    }

    @Override
    public ResponseEntity<Long> getGlossaryVersion() {
        GlossaryVersion glossary = glossaryVersionRepository.findByOrderByVersionDesc();

        if (glossary == null) {
            glossary = new GlossaryVersion();
            glossaryVersionRepository.save(glossary);
        }

        return new ResponseEntity<>(glossary.getVersion(), HttpStatus.OK);
    }

    @Override
    public Long getGlossaryVersionNumber() {
        GlossaryVersion glossary = glossaryVersionRepository.findByOrderByVersionDesc();

        if (glossary == null) {
            glossary = new GlossaryVersion();
            glossary = glossaryVersionRepository.save(glossary);
        }

        return glossary.getVersion();
    }
}
