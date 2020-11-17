package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqUser;
import br.ufrgs.inf.pet.dinoapi.entity.faq.UserQuestion;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqUserRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.UserQuestionRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.alert_update.AlertUpdateQueueServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.service.topic.alert_update.AlertUpdateTopicServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FaqServiceImpl implements FaqService{

    private final FaqRepository faqRepository;

    private final FaqUserRepository faqUserRepository;

    private final UserQuestionRepository userQuestionRepository;

    private final FaqItemServiceImpl faqItemServiceImpl;

    private final AuthServiceImpl authServiceImpl;

    private final AlertUpdateTopicServiceImpl alertUpdateTopicService;

    private final AlertUpdateQueueServiceImpl alertUpdateQueueService;

    @Autowired
    public FaqServiceImpl(FaqRepository faqRepository, FaqUserRepository faqUserRepository, FaqItemServiceImpl faqItemServiceImpl, AuthServiceImpl authServiceImpl,
                          AlertUpdateTopicServiceImpl alertUpdateTopicService, AlertUpdateQueueServiceImpl alertUpdateQueueService,
                          UserQuestionRepository userQuestionRepository) {
        this.faqRepository = faqRepository;
        this.faqUserRepository = faqUserRepository;
        this.faqItemServiceImpl = faqItemServiceImpl;
        this.authServiceImpl = authServiceImpl;
        this.alertUpdateTopicService = alertUpdateTopicService;
        this.alertUpdateQueueService = alertUpdateQueueService;
        this.userQuestionRepository = userQuestionRepository;
    }

    public ResponseEntity<FaqModel> editFaq(FaqModel model) {
        if (model != null && model.getId() != null) {
            final Optional<Faq> faqSearch = faqRepository.findById(model.getId());

            if (faqSearch.isPresent()) {
                final Faq faqDB = faqSearch.get();

                final boolean changed = !model.getTitle().equals(faqDB.getTitle());

                if (changed) {
                    faqDB.setTitle(model.getTitle());
                }

                final boolean itemsChanged = faqItemServiceImpl.editItems(model.getItems(), faqDB);

                if(changed || itemsChanged) {
                    this.updateFaqVersion(faqDB);

                    final List<FaqItem> faqItems = faqItemServiceImpl.getItemsByFaq(faqDB);

                    final FaqModel response = new FaqModel(faqDB, faqItems);

                    return new ResponseEntity<>(response, HttpStatus.OK);
                }

                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<FaqModel> get(FaqIdModel model) {
        if (model != null && model.getId() != null) {
            final Optional<Faq> faqSearch = faqRepository.findById(model.getId());

            if (faqSearch.isPresent()) {
                final Faq faq = faqSearch.get();
                final List<FaqItem> faqItems = faqItemServiceImpl.getItemsByFaq(faq);
                FaqModel response = new FaqModel(faq, faqItems);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<FaqModel>> getAll() {
        final List<Faq> faqSearch = faqRepository.findAllWithFaqItems();

        final List<FaqModel> response = faqSearch.stream().map(FaqModel::new).collect(Collectors.toList());

        if(response.size() > 0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<FaqModel> save(FaqSaveRequestModel faqSaveRequestModel) {
        FaqModel response = new FaqModel();

        if (faqSaveRequestModel != null) {
            final Optional<Faq> faqSearch = faqRepository.findByTitle(faqSaveRequestModel.getTitle());

            final Faq faq = faqSearch.orElseGet(() -> faqRepository.save(new Faq(faqSaveRequestModel)));

            final List<FaqItem> newItems = faqItemServiceImpl.saveItems(faqSaveRequestModel.getItems(), faq);

            if (faqSearch.isPresent() && newItems.size() > 0) {
                this.updateFaqVersion(faq);
            }

            faq.setItems(newItems);

            response = new FaqModel(faq, newItems);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Long> saveFaqUser(FaqIdModel model) {
        if (model != null && model.getId() != null) {
            final Optional<Faq> faqSearch = faqRepository.findById(model.getId());

            if(faqSearch.isPresent()) {
                final User user = authServiceImpl.getCurrentUser();

                FaqUser faqUser = user.getFaqUser();

                final Faq faqDB = faqSearch.get();

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
        final User user = authServiceImpl.getCurrentUser();

        final Optional<FaqUser> faqUserSearch = faqUserRepository.findByUserId(user.getId());

        if(faqUserSearch.isPresent()) {
            final FaqUser faqUser = faqUserSearch.get();
            final List<FaqItem> faqItems = faqItemServiceImpl.getItemsByFaq(faqUser.getFaq());
            final FaqModel response = new FaqModel(faqUser.getFaq(), faqItems);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<List<FaqModel>> saveAll(FaqListSaveRequestModel model) {
        final List<FaqModel> response = new ArrayList<>();

        model.getItems().forEach(faqModel -> {
            if (faqModel != null) {
                final Optional<Faq> faqSearch = faqRepository.findByTitle(faqModel.getTitle());

                final Faq faq = faqSearch.orElseGet(() -> faqRepository.save(new Faq(faqModel)));

                final List<FaqItem> newItems = faqItemServiceImpl.saveItems(faqModel.getItems(), faq);

                if (newItems.size() > 0) {
                    if(faqSearch.isPresent()) {
                        this.updateFaqVersion(faq);
                    }
                    response.add(new FaqModel(faq, newItems));
                }
            }}
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<List<FaqOptionModel>> getFaqOptions() {
        final List<FaqOptionModel> response = Lists.newArrayList(faqRepository.findAll())
                .stream().map(FaqOptionModel::new).collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<FaqVersionModel> getFaqUserVersion() {
        final FaqUser faqUser = authServiceImpl.getCurrentUser().getFaqUser();

        if (faqUser != null) {
            final Faq faq = faqUser.getFaq();

            final FaqVersionModel response = new FaqVersionModel(faq.getId(), faq.getVersion());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<?> saveUserQuestion(UserQuestionSaveRequestModel model) {
        final Optional<Faq> faqSearch = faqRepository.findById(model.getId());

        if(faqSearch.isPresent()) {
            final User user = authServiceImpl.getCurrentUser();
            final UserQuestion userQuestion = new UserQuestion(faqSearch.get(), user, model.getQuestion(), new Date());
            userQuestionRepository.save(userQuestion);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private void updateFaqVersion(Faq faq) {
        faq.updateVersion();
        faqRepository.save(faq);
        alertUpdateTopicService.sendUpdateMessage(faq.getVersion(), faq.getId(), WebSocketDestinationsEnum.ALERT_FAQ_UPDATE);
    }

    private void updateFaqUserId(FaqUser faqUser, Long faqId) {
        faqUserRepository.save(faqUser);
        alertUpdateQueueService.sendUpdateIdMessage(faqId, WebSocketDestinationsEnum.ALERT_FAQ_USER_UPDATE);
    }


}
