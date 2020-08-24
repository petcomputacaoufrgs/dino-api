package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqItemModel;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqSaveRequestItemModel;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FaqItemServiceImpl implements  FaqItemService {

    private final FaqItemRepository faqItemRepository;

    @Autowired
    public FaqItemServiceImpl(FaqItemRepository faqItemRepository) {
        this.faqItemRepository = faqItemRepository;
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

    public boolean editItems(List<FaqItemModel> itemModels, Faq faq) {

        List<FaqItem> itemsToSave = new ArrayList<>();
        List<FaqItem> itemsToDelete = faq.getItems();

        itemModels.forEach(itemModel -> {

            if (itemModel.getId() == null) {
                itemsToSave.add(new FaqItem(itemModel, faq));
            }
            else {

                Optional<FaqItem> itemSearch = itemsToDelete.stream()
                        .filter(item -> item.getId().equals(itemModel.getId()))
                        .findFirst();

                if (itemSearch.isPresent()) {
                    FaqItem itemDB = itemSearch.get();

                    boolean changed = !itemModel.getQuestion().equals(itemDB.getQuestion());
                    if (changed) {
                        itemDB.setQuestion(itemModel.getQuestion());
                    }
                    if (!itemModel.getAnswer().equals(itemDB.getAnswer())) {
                        itemDB.setAnswer(itemModel.getAnswer());
                        changed = true;
                    }
                    if (changed) {
                        itemsToSave.add(itemDB);
                    }
                    itemsToDelete.remove(itemDB);
                }
            }
        });
        faqItemRepository.saveAll(itemsToSave);
        faqItemRepository.deleteAllById(itemsToDelete.stream().map(FaqItem::getId).collect(Collectors.toList()));

        return itemsToSave.size() > 0 || itemsToDelete.size() > 0;
    }
}
