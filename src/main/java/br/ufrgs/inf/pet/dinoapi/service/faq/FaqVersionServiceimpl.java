package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqAllVersion;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqVersion;
import br.ufrgs.inf.pet.dinoapi.repository.faq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FaqVersionServiceimpl {

    private final FaqRepository faqRepository;

    private final FaqItemRepository faqItemRepository;

    private final FaqTypeRepository faqTypeRepository;

    private final FaqItemServiceImpl faqItemServiceImpl;

    private final FaqTypeServiceImpl faqTypeServiceImpl;

    private final FaqVersionRepository faqVersionRepository;

    private final FaqAllVersionRepository faqAllVersionRepository;

    @Autowired
    public FaqVersionServiceimpl(FaqRepository faqRepository, FaqItemRepository faqItemRepository, FaqTypeRepository faqTypeRepository,
                          FaqItemServiceImpl faqItemServiceImpl,
                          FaqTypeServiceImpl faqTypeServiceImpl,
                                 FaqVersionRepository faqVersionRepository,
                                 FaqAllVersionRepository faqAllVersionRepository) {
        this.faqRepository = faqRepository;
        this.faqItemRepository = faqItemRepository;
        this.faqTypeRepository = faqTypeRepository;
        this.faqItemServiceImpl = faqItemServiceImpl;
        this.faqTypeServiceImpl = faqTypeServiceImpl;
        this.faqVersionRepository = faqVersionRepository;
        this.faqAllVersionRepository = faqAllVersionRepository;

    }

    public FaqVersion updateFaqVersion(Faq faq) {
        FaqVersion faqVersion = faqVersionRepository.findVersionDescById(faq.getId());
        if (faqVersion != null) {
            faqVersion.updateVersion();
        } else {
            faqVersion = new FaqVersion(faq);
        }

        faqVersionRepository.save(faqVersion);
        ///@to-do
        // alertUpdateTopicServiceImpl.sendUpdateMessage(glossary.getVersion(), WebSocketDestinationsEnum.ALERT_GLOSSARY_UPDATE);


        return faqVersion;
    }

    public void updateAllFaqVersion() {
        FaqAllVersion faqAllVersion = faqAllVersionRepository.findByOrderByVersionDesc();
        if (faqAllVersion != null) {
            faqAllVersion.updateVersion();
        } else {
            faqAllVersion = new FaqAllVersion();
        }

        faqAllVersionRepository.save(faqAllVersion);
        ///@to-do
        // alertUpdateTopicServiceImpl.sendUpdateMessage(glossary.getVersion(), WebSocketDestinationsEnum.ALERT_GLOSSARY_UPDATE);
    }

    public ResponseEntity<Long> getFaqVersion(Faq faq) {
        FaqVersion faqVersion = faqVersionRepository.findVersionDescById(faq.getId());

        if (faqVersion == null) {
            faqVersion = new FaqVersion(faq);
            faqVersionRepository.save(faqVersion);
        }

        return new ResponseEntity<>(faqVersion.getVersion(), HttpStatus.OK);
    }

    public Long getAllFaqVersionNumber() {
        FaqAllVersion faqAllVersion = faqAllVersionRepository.findByOrderByVersionDesc();
        if (faqAllVersion == null) {
            faqAllVersion = new FaqAllVersion();
            faqAllVersionRepository.save(faqAllVersion);
        }
        return faqAllVersion.getVersion();
    }

    public Long getFaqOptions() {
        FaqAllVersion faqAllVersion = faqAllVersionRepository.findByOrderByVersionDesc();
        if (faqAllVersion != null) {
            faqAllVersion.updateVersion();
        } else {
            faqAllVersion = new FaqAllVersion();
        }

        faqAllVersionRepository.save(faqAllVersion);

        return faqAllVersion.getVersion();
    }

    /*
    public Long getFaqVersionNumber(Long faqId) {
        FaqVersion faqVersion = faqVersionRepository.findVersionDescById(faqId);

        if (faqVersion == null) {
            faqVersion = new FaqVersion();
            faqVersionRepository.save(faqVersion);
        }

        return glossary.getVersion();
    }

     */
}
