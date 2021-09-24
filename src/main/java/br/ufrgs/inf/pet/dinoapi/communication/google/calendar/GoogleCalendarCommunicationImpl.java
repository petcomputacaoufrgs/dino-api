package br.ufrgs.inf.pet.dinoapi.communication.google.calendar;

import br.ufrgs.inf.pet.dinoapi.communication.google.GoogleCommunication;
import br.ufrgs.inf.pet.dinoapi.communication.google.oauth.GoogleOAuthCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.configuration.properties.GoogleOAuth2Config;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.Event;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleAPIURLEnum;
import br.ufrgs.inf.pet.dinoapi.model.google.calendar.GoogleCalendarModel;
import br.ufrgs.inf.pet.dinoapi.model.google.calendar.GoogleEventDateTimeModel;
import br.ufrgs.inf.pet.dinoapi.model.google.calendar.GoogleEventModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserSettingsServiceImpl;
import br.ufrgs.inf.pet.dinoapi.utils.JsonUtils;
import com.google.api.client.util.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneId;

@Service
public class GoogleCalendarCommunicationImpl extends GoogleCommunication {

    final UserSettingsServiceImpl userSettingsService;
    final GoogleOAuth2Config googleOAuth2Config;

    public GoogleCalendarCommunicationImpl(GoogleOAuthCommunicationImpl googleOAuthCommunication,
                                           GoogleAuthServiceImpl googleAuthService,
                                           GoogleOAuth2Config googleOAuth2Config,
                                           UserSettingsServiceImpl userSettingsService,
                                           LogAPIErrorServiceImpl logAPIErrorService) {
        super(logAPIErrorService, googleOAuthCommunication, googleAuthService);
        this.userSettingsService = userSettingsService;
        this.googleOAuth2Config = googleOAuth2Config;
    }


    public void createAndListNewGoogleCalendar(User user) {
        try {
            final GoogleAuth googleAuth = this.getGoogleAuth(user);

            final String accessToken = this.getAccessTokenAndSaveScopes(googleAuth);

            if (accessToken == null) return;

            GoogleCalendarModel newGoogleCalendarModel = new GoogleCalendarModel();
            newGoogleCalendarModel.setSummary("DinoApp Calendar");

            final String jsonModel = JsonUtils.convertToJson(newGoogleCalendarModel);

            final HttpRequest request = this.createBaseRequest(accessToken)
                    .uri(new URI(
                            GoogleAPIURLEnum.CALENDARS.getValue()
                                    + "?key=" + googleOAuth2Config.getAPIkey()))
                    .method("POST", HttpRequest.BodyPublishers.ofString(jsonModel))
                    .build();

            final HttpResponse<String> response = this.send(request);

            if (response.statusCode() == HttpStatus.OK.value()) {

                final GoogleCalendarModel createdCalendar = JsonUtils.convertJsonToObj(response.body(), GoogleCalendarModel.class);

                final UserSettings settings = user.getUserAppSettings();
                settings.setGoogleCalendarId(createdCalendar.getId());
                userSettingsService.saveDirectly(settings);
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            this.logAPIError(e);
        }
    }

    public GoogleEventModel createNewEvent(User user, Event event) {
        try {
            final GoogleAuth googleAuth = this.getGoogleAuth(user);

            final String accessToken = this.getAccessTokenAndSaveScopes(googleAuth);

            if (accessToken == null) return null;

            final String googleCalendarId = user.getUserAppSettings().getGoogleCalendarId();

            if(googleCalendarId == null) return null;

            String timeZone = ZoneId.SHORT_IDS.get(event.getDate().getZone().getId());

            GoogleEventModel newGoogleEventModel = new GoogleEventModel();
            newGoogleEventModel.setSummary(event.getTitle());
            newGoogleEventModel.setDescription(event.getDescription());

            GoogleEventDateTimeModel start = new GoogleEventDateTimeModel();
            start.setDateTime(new DateTime(event.getDate().toLocalDateTime().toString()));
            start.setTimeZone(timeZone);

            GoogleEventDateTimeModel end = new GoogleEventDateTimeModel();
            end.setDateTime(new DateTime(event.getEndTime().toLocalDateTime().toString()));
            end.setTimeZone(timeZone);

            newGoogleEventModel.setStart(start);
            newGoogleEventModel.setEnd(end);

            final String jsonModel = JsonUtils.convertToJson(newGoogleEventModel);

            final HttpRequest request = this.createBaseRequest(accessToken)
                    .uri(new URI(
                            GoogleAPIURLEnum.CALENDARS.getValue()
                                    + "/" + googleCalendarId + "/events"
                                    + "?key=" + googleOAuth2Config.getAPIkey()))
                    .method("POST", HttpRequest.BodyPublishers.ofString(jsonModel))
                    .build();

            final HttpResponse<String> response = this.send(request);

            if (response.statusCode() == HttpStatus.OK.value()) {

                return JsonUtils.convertJsonToObj(response.body(), GoogleEventModel.class);
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            this.logAPIError(e);
        }

        return null;
    }
}
