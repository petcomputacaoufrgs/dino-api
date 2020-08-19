package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqItemRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FaqTypeServiceImpl {

    private final FaqRepository faqRepository;

    private final FaqItemRepository faqItemRepository;

    private final FaqTypeRepository faqTypeRepository;

    private final FaqItemServiceImpl faqItemServiceImpl;

    @Autowired
    public FaqTypeServiceImpl(FaqRepository faqRepository, FaqItemRepository faqItemRepository, FaqTypeRepository faqTypeRepository,
                          FaqItemServiceImpl faqItemServiceImpl) {
        this.faqRepository = faqRepository;
        this.faqItemRepository = faqItemRepository;
        this.faqTypeRepository = faqTypeRepository;
        this.faqItemServiceImpl = faqItemServiceImpl;
    }
}
