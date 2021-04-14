package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.communication.google.people.GooglePeopleCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.model.google.people.GooglePeopleModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.GoogleContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleScopeServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GoogleContactServiceImpl extends LogUtilsBase {
    private final GoogleContactRepository repository;
    private final GoogleScopeServiceImpl googleScopeService;
    private final PhoneRepository phoneRepository;
    private final GooglePeopleCommunicationImpl googlePeopleCommunication;

    @Autowired
    public GoogleContactServiceImpl(GoogleContactRepository repository, LogAPIErrorServiceImpl logAPIErrorService,
                                    GoogleScopeServiceImpl googleScopeService, PhoneRepository phoneRepository,
                                    GooglePeopleCommunicationImpl googlePeopleCommunication) {
        super(logAPIErrorService);
        this.repository = repository;
        this.googleScopeService = googleScopeService;
        this.phoneRepository = phoneRepository;
        this.googlePeopleCommunication = googlePeopleCommunication;
    }

    public void createNewGoogleContact(Contact entity, User user) {
        if (googleScopeService.hasGoogleContactScope(user)) {
            final List<String> phoneNumbers = phoneRepository.findAllPhoneNumbersByContactId(entity.getId());

            final GooglePeopleModel googlePeopleModel =
                    googlePeopleCommunication.createContact(
                            user, entity.getName(), entity.getDescription(), phoneNumbers
                    );

            if (googlePeopleModel != null) {
                final GoogleContact googleContact = new GoogleContact();
                googleContact.setContact(entity);
                googleContact.setUser(user);
                googleContact.setResourceName(googlePeopleModel.getResourceName());
                this.save(googleContact);
            }
        }
    }

    public void updateGoogleContact(Contact contact, GoogleContact googleContact) {
        final User user = contact.getUser();
        if (googleScopeService.hasGoogleContactScope(user)) {
            final List<String> phoneNumbers = phoneRepository.findAllPhoneNumbersByContactId(contact.getId());

            final GooglePeopleModel googlePeopleModel = googlePeopleCommunication.
                    updateContact(
                            user, contact.getName(), contact.getDescription(),
                            phoneNumbers, googleContact
                    );

            if (googlePeopleModel != null) {
                googleContact.setResourceName(googleContact.getResourceName());
                this.save(googleContact);
            }
        }
    }

    public void deleteGoogleContact(String resourceName, User user) {
        if (googleScopeService.hasGoogleContactScope(user)) {
            googlePeopleCommunication.deleteContact(user, resourceName);
        }
    }

    public void save(GoogleContact entity) {
        try {
            this.repository.save(entity);
        } catch (Exception e) {
            this.logAPIError(e);
        }
    }

    public void delete(GoogleContact entity) {
        try {
            this.repository.delete(entity);
        } catch (Exception e) {
            this.logAPIError(e);
        }
    }

    public Optional<GoogleContact> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUser(id, auth);
    }

    public Optional<GoogleContact> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUser(id, auth);
    }

    private Optional<GoogleContact> findByIdAndUser(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    public List<GoogleContact> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return repository.findAllByUserId(auth.getUser().getId());
    }

    public List<GoogleContact> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return repository.findAllByIdAndUserId(ids, auth.getUser().getId());
    }

    public List<GoogleContact> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return repository.findAllByUserIdExcludingIds(auth.getUser().getId(), ids);
    }

    public Optional<GoogleContact> findByContactId(Long contactId) {
        return repository.findByContactId(contactId);
    }

    public List<GoogleContact> findAllByUserOrderByContactId(User user) {
        return repository.findAllByUserOrderByContactId(user.getId());
    }
}
