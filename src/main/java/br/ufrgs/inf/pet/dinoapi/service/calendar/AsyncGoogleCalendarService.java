package br.ufrgs.inf.pet.dinoapi.service.calendar;

import br.ufrgs.inf.pet.dinoapi.communication.google.calendar.GoogleCalendarCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class AsyncGoogleCalendarService extends LogUtilsBase {

    final GoogleCalendarCommunicationImpl googleCalendarCommunication;

    @Autowired
    public AsyncGoogleCalendarService(LogAPIErrorServiceImpl logAPIErrorService,
                                     @Lazy GoogleCalendarCommunicationImpl googleCalendarCommunication) {
        super(logAPIErrorService);
        this.googleCalendarCommunication = googleCalendarCommunication;
    }

    //    @Async("defaultThreadPoolTaskExecutor")
        public void updateGoogleCalendar() {
            try {
                googleCalendarCommunication.createAndListNewGoogleCalendar();
            } catch (Exception e) {
                this.logAPIError(e);
            }
        }
}

