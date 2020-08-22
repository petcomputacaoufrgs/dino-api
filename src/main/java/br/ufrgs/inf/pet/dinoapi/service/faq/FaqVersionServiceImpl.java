package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqVersion;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqsVersion;
import br.ufrgs.inf.pet.dinoapi.repository.faq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FaqVersionServiceImpl {

    private final FaqRepository faqRepository;

    private final FaqItemRepository faqItemRepository;

    private final FaqUserRepository faqUserRepository;

    private final FaqItemServiceImpl faqItemServiceImpl;

    private final FaqVersionRepository faqVersionRepository;

    private final FaqsVersionRepository faqsVersionRepository;

    @Autowired
    public FaqVersionServiceImpl(FaqRepository faqRepository, FaqItemRepository faqItemRepository, FaqUserRepository faqUserRepository,
                                 FaqItemServiceImpl faqItemServiceImpl,
                                 FaqVersionRepository faqVersionRepository,
                                 FaqsVersionRepository faqsVersionRepository) {
        this.faqRepository = faqRepository;
        this.faqItemRepository = faqItemRepository;
        this.faqUserRepository = faqUserRepository;
        this.faqItemServiceImpl = faqItemServiceImpl;
        this.faqVersionRepository = faqVersionRepository;
        this.faqsVersionRepository = faqsVersionRepository;

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

    public void updateFaqsVersion() {
        FaqsVersion faqsVersion = faqsVersionRepository.findByOrderByVersionDesc();
        if (faqsVersion != null) {
            faqsVersion.updateVersion();
        } else {
            faqsVersion = new FaqsVersion();
        }

        faqsVersionRepository.save(faqsVersion);
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

    public Long getFaqsVersionNumber() {
        FaqsVersion faqsVersion = faqsVersionRepository.findByOrderByVersionDesc();
        if (faqsVersion == null) {
            faqsVersion = new FaqsVersion();
            faqsVersionRepository.save(faqsVersion);
        }
        return faqsVersion.getVersion();
    }

}
