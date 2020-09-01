package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqItemModel;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqSaveRequestItemModel;

import java.util.List;

public interface FaqItemService {

    public List<FaqItem> saveItems(List<FaqSaveRequestItemModel> models, Faq faq);

    public boolean editItems(List<FaqItemModel> itemModels, Faq faq);
}
