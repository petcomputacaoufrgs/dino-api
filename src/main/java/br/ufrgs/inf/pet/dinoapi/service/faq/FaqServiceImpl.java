package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqModel;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqSaveRequestModel;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqItemRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqTypeRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FaqServiceImpl {

        private final FaqRepository faqRepository;

        private final FaqItemRepository faqItemRepository;

        private final FaqTypeRepository faqTypeRepository;

        private final FaqItemServiceImpl faqItemServiceImpl;

        private final FaqTypeServiceImpl faqTypeServiceImpl;

        private final FaqVersionServiceimpl faqVersionServiceimpl;

        @Autowired
        public FaqServiceImpl(FaqRepository faqRepository, FaqItemRepository faqItemRepository, FaqTypeRepository faqTypeRepository,
                              FaqItemServiceImpl faqItemServiceImpl,
                              FaqTypeServiceImpl faqTypeServiceImpl,
                              FaqVersionServiceimpl faqVersionServiceimpl) {
            this.faqRepository = faqRepository;
            this.faqItemRepository = faqItemRepository;
            this.faqTypeRepository = faqTypeRepository;
            this.faqItemServiceImpl = faqItemServiceImpl;
            this.faqTypeServiceImpl = faqTypeServiceImpl;
            this.faqVersionServiceimpl = faqVersionServiceimpl;

        }

        public ResponseEntity<List<FaqModel>> getAll() {

            List<Faq> contacts = Lists.newArrayList(faqRepository.findAll());

            List<FaqModel> response = contacts.stream().map(FaqModel::new).collect(Collectors.toList());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        public ResponseEntity<FaqModel> save(FaqSaveRequestModel faqSaveRequestModel) {
            FaqModel response = new FaqModel();

            if (faqSaveRequestModel != null) {

                //fazer validação

                Faq faq = faqRepository.save(new Faq(faqSaveRequestModel));

                List<FaqItem> newItems = faqItemServiceImpl.saveItems(faqSaveRequestModel.getItems(), faq);

                faq.setItems(newItems);

                faq.setVersion(faqVersionServiceimpl.updateFaqVersion(faq));

                response = new FaqModel(faq);

            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    public ResponseEntity<List<FaqModel>> saveAll(List<FaqSaveRequestModel> models) {
        List<FaqModel> response = new ArrayList<>();

        models.forEach(model -> {

            if (model != null) {

                //fazer validação

                Faq faq = faqRepository.save(new Faq(model));

                List<FaqItem> newItems = faqItemServiceImpl.saveItems(model.getItems(), faq);

                faq.setItems(newItems);

                faq.setVersion(faqVersionServiceimpl.updateFaqVersion(faq));

                response.add(new FaqModel(faq));

            }

        }
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

        /*
        public ResponseEntity<?> update(GlossaryUpdateRequestModel glossaryUpdateRequestModel) {
            final GlossaryResponseModel response = new GlossaryResponseModel();
            Long glossaryVersion = glossaryVersionService.getGlossaryVersionNumber();

            if (glossaryUpdateRequestModel.getVersion() != glossaryVersion) {
                return new ResponseEntity<>("Versão do glossário inválida.", HttpStatus.BAD_REQUEST);
            }

            if (glossaryUpdateRequestModel != null) {
                final List<GlossaryItemUpdateRequestModel> updatedItemsList = glossaryUpdateRequestModel.getItemList();

                if (updatedItemsList != null) {
                    Optional<GlossaryItem> glossaryItemSearchResult;
                    GlossaryItem dbGlossaryItem;
                    Boolean updated;
                    GlossaryItemResponseModel responseItem;

                    for (GlossaryItemUpdateRequestModel updatedItem : updatedItemsList) {
                        if (updatedItem.isValid()) {
                            glossaryItemSearchResult = glossaryItemRepository.findById(updatedItem.getId());

                            if (glossaryItemSearchResult.isPresent()) {
                                dbGlossaryItem = glossaryItemSearchResult.get();

                                updated = dbGlossaryItem.update(updatedItem);

                                if (updated) {

                                    dbGlossaryItem = glossaryItemRepository.save(dbGlossaryItem);

                                    responseItem = new GlossaryItemResponseModel();

                                    responseItem.setByGlossaryItem(dbGlossaryItem);

                                    response.addItem(responseItem);
                                }
                            }
                        }
                    }


                    if (response.getSize() > 0) {
                        glossaryVersion = glossaryVersionService.updateGlossaryVersion();
                    }

                    response.setVersion(glossaryVersion);

                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }

            return new ResponseEntity<>("Erro: GLossário vazio", HttpStatus.BAD_REQUEST);
        }

        public ResponseEntity<List<GlossaryItem>> get() {
            GlossaryItemResponseModel responseItem;

            final List<GlossaryItem> response = glossaryItemRepository.findAllByExistsTrue();

            for (GlossaryItem item : response) {
                responseItem = new GlossaryItemResponseModel();

                responseItem.setByGlossaryItem(item);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

         */

}
