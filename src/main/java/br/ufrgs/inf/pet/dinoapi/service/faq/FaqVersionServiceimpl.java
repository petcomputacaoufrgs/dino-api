package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqVersion;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqItemRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqTypeRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqVersionRepository;
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

    @Autowired
    public FaqVersionServiceimpl(FaqRepository faqRepository, FaqItemRepository faqItemRepository, FaqTypeRepository faqTypeRepository,
                          FaqItemServiceImpl faqItemServiceImpl,
                          FaqTypeServiceImpl faqTypeServiceImpl,
                                 FaqVersionRepository faqVersionRepository) {
        this.faqRepository = faqRepository;
        this.faqItemRepository = faqItemRepository;
        this.faqTypeRepository = faqTypeRepository;
        this.faqItemServiceImpl = faqItemServiceImpl;
        this.faqTypeServiceImpl = faqTypeServiceImpl;
        this.faqVersionRepository = faqVersionRepository;

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

    public ResponseEntity<Long> getFaqVersion(Faq faq) {
        FaqVersion faqVersion = faqVersionRepository.findVersionDescById(faq.getId());

        if (faqVersion == null) {
            faqVersion = new FaqVersion(faq);
            faqVersionRepository.save(faqVersion);
        }

        return new ResponseEntity<>(faqVersion.getVersion(), HttpStatus.OK);
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
