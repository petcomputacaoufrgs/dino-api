package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqUser;
import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqUserRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FaqServiceImpl {

        private final FaqRepository faqRepository;

        private final FaqUserRepository faqUserRepository;

        private final FaqItemServiceImpl faqItemServiceImpl;

        private final FaqVersionServiceImpl faqVersionServiceimpl;

        private final AuthServiceImpl authServiceImpl;


    @Autowired
        public FaqServiceImpl(FaqRepository faqRepository,
                              FaqUserRepository faqUserRepository,
                              FaqItemServiceImpl faqItemServiceImpl,
                              FaqVersionServiceImpl faqVersionServiceimpl,
                              AuthServiceImpl authServiceImpl) {
            this.faqRepository = faqRepository;
            this.faqUserRepository = faqUserRepository;
            this.faqItemServiceImpl = faqItemServiceImpl;
            this.faqVersionServiceimpl = faqVersionServiceimpl;
            this.authServiceImpl = authServiceImpl;

        }

        public ResponseEntity<FaqModel> get(FaqIdModel model) {

            if (model != null && model.getId() != null) {
                Optional<Faq> faqSearch = faqRepository.findById(model.getId());

                if (faqSearch.isPresent()) {
                    FaqModel response = new FaqModel(faqSearch.get());
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        public ResponseEntity<FaqAllModel> getAll() {

            List<Faq> contacts = Lists.newArrayList(faqRepository.findAll());

            List<FaqModel> faqs = contacts.stream().map(FaqModel::new).collect(Collectors.toList());

            FaqAllModel response = new FaqAllModel(faqVersionServiceimpl.getFaqsVersionNumber(), faqs);

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

            faqVersionServiceimpl.updateFaqsVersion();

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    public ResponseEntity<Long> saveFaqUser(FaqIdModel model) {

        User user = authServiceImpl.getCurrentUser();

        if (model != null) {

            FaqUser faqUser = user.getFaqUser();

            //fazer validação e ver se o id mudou

            Optional<Faq> faqSearch = faqRepository.findById(model.getId());

            if(faqSearch.isPresent()) {

                if(faqUser != null) {

                    faqUser.setFaq(faqSearch.get());

                    faqUserRepository.save(faqUser);

                } else {
                    faqUserRepository.save(new FaqUser(faqSearch.get(), user));
                }

                return new ResponseEntity<>(model.getId(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<FaqModel> getFaqUser() {

        User user = authServiceImpl.getCurrentUser();

        FaqUser faqUser = user.getFaqUser();

        if(faqUser != null) {
            FaqModel response = new FaqModel(faqUser.getFaq());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
            }}
        );

        faqVersionServiceimpl.updateFaqsVersion();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<FaqOptionsModel> getFaqOptions(){

        final List<FaqOptionModel> options = Lists.newArrayList(faqRepository.findAll())
                .stream().map(FaqOptionModel::new).collect(Collectors.toList());

        final Long version = faqVersionServiceimpl.getFaqsVersionNumber();

        return new ResponseEntity<>(new FaqOptionsModel(version, options), HttpStatus.OK);

    }

    public ResponseEntity<Long> getFaqOptionsVersion(){

        final Long version = faqVersionServiceimpl.getFaqsVersionNumber();

        return new ResponseEntity<>(version, HttpStatus.OK);

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
