package br.ufrgs.inf.pet.dinoapi.service.calendar;

import br.ufrgs.inf.pet.dinoapi.communication.google.calendar.GoogleCalendarCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class AsyncGoogleCalendarServiceImpl extends LogUtilsBase {

    final GoogleCalendarCommunicationImpl googleCalendarCommunication;

    @Autowired
    public AsyncGoogleCalendarServiceImpl(LogAPIErrorServiceImpl logAPIErrorService,
                                          @Lazy GoogleCalendarCommunicationImpl googleCalendarCommunication) {
        super(logAPIErrorService);
        this.googleCalendarCommunication = googleCalendarCommunication;
    }

    //    @Async("defaultThreadPoolTaskExecutor")
        public void createGoogleCalendar(User user) {
            try {
                googleCalendarCommunication.createAndListNewGoogleCalendar(user);
            } catch (Exception e) {
                this.logAPIError(e);
            }
        }

        public void createEventOnGoogleAPI(User user) {
            UserSettings userSettings = user.getUserAppSettings();
            String calendarId = userSettings.getGoogleCalendarId();
            if(calendarId != null) {

            } else {
                createGoogleCalendar(user);
                calendarId = userSettings.getGoogleCalendarId();
            }
        }
}

