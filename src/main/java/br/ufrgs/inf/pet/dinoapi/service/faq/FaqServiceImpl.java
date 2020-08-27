package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.entity.faq.*;
import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqUserRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.queue.AlertUpdateQueueServiceImpl;
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

        private final AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl;

    @Autowired
        public FaqServiceImpl(FaqRepository faqRepository, FaqUserRepository faqUserRepository, FaqItemServiceImpl faqItemServiceImpl, AuthServiceImpl authServiceImpl,
                              AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl) {
            this.faqRepository = faqRepository;
            this.faqUserRepository = faqUserRepository;
            this.faqItemServiceImpl = faqItemServiceImpl;
            this.authServiceImpl = authServiceImpl;
            this.alertUpdateQueueServiceImpl = alertUpdateQueueServiceImpl;
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
                        faqDB.updateVersion();
                    }

                    faqRepository.save(faqDB);

                    FaqModel response = new FaqModel(faqDB);

                    //alertUpdateQueueServiceImpl.sendUpdateMessage(faqDB.getVersion(), WebSocketDestinationsEnum.ALERT_FAQ_UPDATE);

                    return new ResponseEntity<>(response, HttpStatus.OK);
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

                //fazer validação

                Faq faq = faqRepository.save(new Faq(faqSaveRequestModel));

                List<FaqItem> newItems = faqItemServiceImpl.saveItems(faqSaveRequestModel.getItems(), faq);

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

                faqUserRepository.save(faqUser);

                alertUpdateQueueServiceImpl.sendUpdateMessage(faqDB.getId(), WebSocketDestinationsEnum.ALERT_FAQ_USER_UPDATE);

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

    public ResponseEntity<List<FaqModel>> saveAll(List<FaqSaveRequestModel> models) {
        List<FaqModel> response = new ArrayList<>();

        models.forEach(model -> {

            if (model != null) {

                //fazer validação

                Faq faq = faqRepository.save(new Faq(model));

                List<FaqItem> newItems = faqItemServiceImpl.saveItems(model.getItems(), faq);

                faq.setItems(newItems);

                response.add(new FaqModel(faq));
            }}
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<List<FaqOptionModel>> getFaqOptions() {

        final List<FaqOptionModel> response = Lists.newArrayList(faqRepository.findAll())
                .stream().map(FaqOptionModel::new).collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public ResponseEntity<FaqSyncModel> getFaqUserVersion() {

        final FaqUser faqUser = authServiceImpl.getCurrentUser().getFaqUser();

        final Faq faq = faqUser.getFaq();

        FaqSyncModel response = new FaqSyncModel(faq.getId(), faq.getVersion());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
