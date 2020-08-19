package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqSaveRequestItemModel;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqItemRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FaqItemServiceImpl {

    private final FaqRepository faqRepository;

    private final FaqItemRepository faqItemRepository;

    private final FaqTypeRepository faqTypeRepository;

    @Autowired
    public FaqItemServiceImpl(FaqRepository faqRepository, FaqItemRepository faqItemRepository, FaqTypeRepository faqTypeRepository) {
        this.faqRepository = faqRepository;
        this.faqItemRepository = faqItemRepository;
        this.faqTypeRepository = faqTypeRepository;
    }

    public List<FaqItem> saveItems(List<FaqSaveRequestItemModel> models, Faq faq) {

        Optional<FaqItem> faqItemSearchResult;
        List<FaqItem> itemsResponse = new ArrayList<>();

        if (models != null) {
            for (FaqSaveRequestItemModel newItem : models) {
                if (newItem.isValid()) {
                    faqItemSearchResult = faqItemRepository.findByQuestion(newItem.getQuestion());

                    if (faqItemSearchResult.isEmpty()) {
                        FaqItem faqItem = new FaqItem(newItem, faq);
                        faqItemRepository.save(faqItem);
                        itemsResponse.add(faqItem);
                    }
                }
            }
        }

        return itemsResponse;
    }
}
