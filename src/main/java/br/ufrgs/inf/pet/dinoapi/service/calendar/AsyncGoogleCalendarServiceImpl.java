package br.ufrgs.inf.pet.dinoapi.service.calendar;

import br.ufrgs.inf.pet.dinoapi.communication.google.calendar.GoogleCalendarCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.Event;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.GoogleEvent;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.repository.calendar.EventRepository;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AsyncGoogleCalendarServiceImpl extends LogUtilsBase {

    final GoogleCalendarCommunicationImpl googleCalendarCommunication;
    final EventRepository eventRepository;
    final GoogleCalendarServiceImpl googleCalendarService;

    @Autowired
    public AsyncGoogleCalendarServiceImpl(LogAPIErrorServiceImpl logAPIErrorService,
                                          @Lazy GoogleCalendarCommunicationImpl googleCalendarCommunication,
                                          EventRepository eventRepository,
                                          @Lazy GoogleCalendarServiceImpl googleCalendarService) {
        super(logAPIErrorService);
        this.googleCalendarCommunication = googleCalendarCommunication;
        this.eventRepository = eventRepository;
        this.googleCalendarService = googleCalendarService;
    }


    //@Async("contactThreadPoolTaskExecutor")
    public void createEventOnGoogleAPI(Event entity, Auth auth) {
        final User user = auth.getUser();

        googleCalendarService.createGoogleEvent(entity, user);
    }

    //@Async("contactThreadPoolTaskExecutor")
    public void updateEventOnGoogleAPI(Event entity, Auth auth) {
        final User user = auth.getUser();

        final Optional<GoogleEvent> googleEventSearch = this.googleCalendarService.findByEventId(entity.getId());

        if (googleEventSearch.isPresent()) {
            googleCalendarService.updateGoogleEvent(entity, googleEventSearch.get());
        } else googleCalendarService.createGoogleEvent(entity, user);

    }

    //@Async("contactThreadPoolTaskExecutor")
    public void deleteEventOnGoogleAPI(GoogleEvent googleEvent, Auth auth) {
        final User user = auth.getUser();
        final boolean hasUserPermission = user.getPermission().equals(PermissionEnum.USER.getValue());

        if (hasUserPermission) {
            googleCalendarService.deleteGoogleEvent(googleEvent, user);
        }
    }

    /*
        Used only when the user general settings are updated to grant access to google calendar previously blocked.
    */
    //@Async("contactThreadPoolTaskExecutor")
    public void asyncUpdateAllUserGoogleEvents(User user) {
        try {

            if(user.getUserAppSettings().getGoogleCalendarId() == null) {
                googleCalendarService.createGoogleCalendar(user);
            }

            final List<Event> events = eventRepository.findAllByUserOrderById(user.getId());
            final List<GoogleEvent> googleEvents = googleCalendarService.findAllByUserOrderByEventId(user);
            int index = 0;
            for(Event event : events) {
                GoogleEvent googleEvent = null;
                boolean hasGoogleEvent = false;

                if (index < googleEvents.size()) {
                    googleEvent = googleEvents.get(index);
                    hasGoogleEvent = googleEvent.getEvent().getId().equals(event.getId());
                }

                if (hasGoogleEvent) {
                    googleCalendarService.updateGoogleEvent(event, googleEvent);
                    index++;
                } else googleCalendarService.createGoogleEvent(event, user);
            }
        } catch (Exception e) {
            this.logAPIError(e);
        }
    }
}

