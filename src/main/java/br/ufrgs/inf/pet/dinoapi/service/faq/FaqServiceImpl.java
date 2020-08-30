package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqUser;
import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqUserRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.queue.AlertUpdateQueueServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.topic.AlertUpdateTopicServiceImpl;
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
public class FaqServiceImpl implements FaqService{

    private final FaqRepository faqRepository;

    private final FaqUserRepository faqUserRepository;

    private final FaqItemServiceImpl faqItemServiceImpl;

    private final AuthServiceImpl authServiceImpl;

    private final AlertUpdateTopicServiceImpl alertUpdateTopicService;

    private final AlertUpdateQueueServiceImpl alertUpdateQueueService;

    @Autowired
    public FaqServiceImpl(FaqRepository faqRepository, FaqUserRepository faqUserRepository, FaqItemServiceImpl faqItemServiceImpl, AuthServiceImpl authServiceImpl,
                          AlertUpdateTopicServiceImpl alertUpdateTopicService, AlertUpdateQueueServiceImpl alertUpdateQueueService) {
        this.faqRepository = faqRepository;
        this.faqUserRepository = faqUserRepository;
        this.faqItemServiceImpl = faqItemServiceImpl;
        this.authServiceImpl = authServiceImpl;
        this.alertUpdateTopicService = alertUpdateTopicService;
        this.alertUpdateQueueService = alertUpdateQueueService;
    }

    public ResponseEntity<FaqModel> editFaq(FaqModel model) {

        if (model != null && model.getId() != null) {
            Optional<Faq> faqSearch = faqRepository.findById(model.getId());

            if (faqSearch.isPresent()) {

                Faq faqDB = faqSearch.get();

                boolean changed = !model.getTitle().equals(faqDB.getTitle());

                if (changed) {
                    faqDB.setTitle(model.getTitle());
                }

                boolean itemsChanged = faqItemServiceImpl.editItems(model.getItems(), faqDB);

                if(changed || itemsChanged) {

                    this.updateFaqVersion(faqDB);

                    FaqModel response = new FaqModel(faqDB);

                    return new ResponseEntity<>(response, HttpStatus.OK);
                }

                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

    public ResponseEntity<FaqModel> save(FaqSaveRequestModel faqSaveRequestModel) {
        FaqModel response = new FaqModel();

        if (faqSaveRequestModel != null) {

            Faq faq;

            Optional<Faq> faqSearch = faqRepository.findByTitle(faqSaveRequestModel.getTitle());

            faq = faqSearch.orElseGet(() -> faqRepository.save(new Faq(faqSaveRequestModel)));

            List<FaqItem> newItems = faqItemServiceImpl.saveItems(faqSaveRequestModel.getItems(), faq);

            if (faqSearch.isPresent() && newItems.size() > 0) {
                this.updateFaqVersion(faq);
            }

            faq.setItems(newItems);

            response = new FaqModel(faq);

        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Long> saveFaqUser(FaqIdModel model) {
        if (model != null && model.getId() != null) {

            Optional<Faq> faqSearch = faqRepository.findById(model.getId());

            if(faqSearch.isPresent()) {

                User user = authServiceImpl.getCurrentUser();

                FaqUser faqUser = user.getFaqUser();

                Faq faqDB = faqSearch.get();

                if(faqUser != null) {
                    faqUser.setFaq(faqDB);
                } else {
                    faqUser = new FaqUser(faqDB, user);
                }

                this.updateFaqUserId(faqUser, faqDB.getId());

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

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //tem suporte para editar RESPOSTAS (através da question) e adicionar ITEMS de faqs existentes (através da title), não tem como excluir ITEMS (usar o edit)
    public ResponseEntity<List<FaqModel>> saveAll(FaqListSaveRequestModel model) {

        List<FaqModel> response = new ArrayList<>();

        model.getItems().forEach(faqModel -> {

            if (faqModel != null) {

                Optional<Faq> faqSearch = faqRepository.findByTitle(faqModel.getTitle());

                Faq faq = faqSearch.orElseGet(() -> faqRepository.save(new Faq(faqModel)));

                List<FaqItem> newItems = faqItemServiceImpl.saveItems(faqModel.getItems(), faq);

                faq.setItems(newItems);

                if (newItems.size() > 0) { //para aparecer só as models MODIFICADAS na response JUNTO com os novos items
                    if(faqSearch.isPresent()) {
                        this.updateFaqVersion(faq);
                    }
                    response.add(new FaqModel(faq));
                }
            }}
        );

        if(response.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<List<FaqOptionModel>> getFaqOptions() {

        final List<FaqOptionModel> response = Lists.newArrayList(faqRepository.findAll())
                .stream().map(FaqOptionModel::new).collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public ResponseEntity<FaqSyncModel> getFaqUserVersion() {

        final FaqUser faqUser = authServiceImpl.getCurrentUser().getFaqUser();

        if (faqUser != null) {
            final Faq faq = faqUser.getFaq();

            FaqSyncModel response = new FaqSyncModel(faq.getId(), faq.getVersion());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void updateFaqVersion(Faq faq) {
        faq.updateVersion();
        faqRepository.save(faq);
        alertUpdateTopicService.sendUpdateMessage(faq.getVersion(), WebSocketDestinationsEnum.ALERT_FAQ_UPDATE);
    }

    private void updateFaqUserId(FaqUser faqUser, Long faqId) {
        faqUserRepository.save(faqUser);
        alertUpdateQueueService.sendUpdateIdMessage(faqId, WebSocketDestinationsEnum.ALERT_FAQ_USER_UPDATE);
    }


}
