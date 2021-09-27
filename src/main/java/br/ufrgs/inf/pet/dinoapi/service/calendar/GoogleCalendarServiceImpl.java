package br.ufrgs.inf.pet.dinoapi.service.calendar;

import br.ufrgs.inf.pet.dinoapi.communication.google.calendar.GoogleCalendarCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.Event;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.GoogleEvent;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleScopeURLEnum;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.model.google.calendar.GoogleCalendarModel;
import br.ufrgs.inf.pet.dinoapi.model.google.calendar.GoogleEventModel;
import br.ufrgs.inf.pet.dinoapi.repository.calendar.GoogleEventRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleScopeServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import br.ufrgs.inf.pet.dinoapi.service.user.UserSettingsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GoogleCalendarServiceImpl extends LogUtilsBase {
    private final GoogleEventRepository repository;
    private final GoogleScopeServiceImpl googleScopeService;
    private final GoogleCalendarCommunicationImpl googleCalendarCommunication;
    private final UserSettingsServiceImpl userSettingsService;

    @Autowired
    public GoogleCalendarServiceImpl(GoogleEventRepository repository, LogAPIErrorServiceImpl logAPIErrorService,
                                     GoogleScopeServiceImpl googleScopeService,
                                     GoogleCalendarCommunicationImpl googleCalendarCommunication,
                                     UserSettingsServiceImpl userSettingsService) {
        super(logAPIErrorService);
        this.repository = repository;
        this.googleScopeService = googleScopeService;
        this.googleCalendarCommunication = googleCalendarCommunication;
        this.userSettingsService = userSettingsService;
    }

    public void createGoogleCalendar(User user) {
        try {
            GoogleCalendarModel createdCalendar = googleCalendarCommunication.createAndListNewGoogleCalendar(user);
            if(createdCalendar != null) {
                final UserSettings settings = user.getUserAppSettings();
                settings.setGoogleCalendarId(createdCalendar.getId());
                userSettingsService.saveDirectly(settings);
            }

        } catch (Exception e) {
            this.logAPIError(e);
        }
    }

    public void createGoogleEvent(Event entity, User user) {
        if (googleScopeService.hasGoogleScope(user, GoogleScopeURLEnum.SCOPE_CALENDAR)) {

            final GoogleEventModel googleEventModel = googleCalendarCommunication.insertGoogleEvent(user, entity);

            if (googleEventModel != null) {
                final GoogleEvent googleEvent = new GoogleEvent();
                googleEvent.setEvent(entity);
                googleEvent.setGoogleId(googleEventModel.getId());
                this.save(googleEvent);
            }
        }
    }

    public void updateGoogleEvent(Event event, GoogleEvent googleEvent) {
        final User user = event.getUser();
        if (googleScopeService.hasGoogleScope(user, GoogleScopeURLEnum.SCOPE_CALENDAR)) {

            final GoogleEventModel googleEventModel = googleCalendarCommunication.updateGoogleEvent(user, event, googleEvent);

            if (googleEventModel != null) {
                googleEvent.setGoogleId(googleEventModel.getId());
                this.save(googleEvent);
            }
        }
    }

    //TODO onde chamar
    public void deleteGoogleEvent(GoogleEvent googleEvent, User user) {
        googleCalendarCommunication.deleteGoogleEvent(user, googleEvent);
        this.delete(googleEvent);
    }


    private void save(GoogleEvent entity) {
        try {
            this.repository.save(entity);
        } catch (Exception e) {
            this.logAPIError(e);
        }
    }

    private void delete(GoogleEvent entity) {
        try {
            this.repository.delete(entity);
        } catch (Exception e) {
            this.logAPIError(e);
        }
    }

    public Optional<GoogleEvent> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUser(id, auth);
    }

    public Optional<GoogleEvent> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUser(id, auth);
    }

    private Optional<GoogleEvent> findByIdAndUser(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    public List<GoogleEvent> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return repository.findAllByUserId(auth.getUser().getId());
    }

    public List<GoogleEvent> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return repository.findAllByIdAndUserId(ids, auth.getUser().getId());
    }

    public List<GoogleEvent> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return repository.findAllByUserIdExcludingIds(auth.getUser().getId(), ids);
    }

    public Optional<GoogleEvent> findByEventId(Long eventId) {
        return repository.findByEventId(eventId);
    }

    public List<GoogleEvent> findAllByUserOrderByEventId(User user) {
        return repository.findAllByUserOrderByEventId(user.getId());
    }
}
