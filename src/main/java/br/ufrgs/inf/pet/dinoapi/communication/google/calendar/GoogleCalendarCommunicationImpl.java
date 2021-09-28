package br.ufrgs.inf.pet.dinoapi.communication.google.calendar;

import br.ufrgs.inf.pet.dinoapi.communication.google.GoogleCommunication;
import br.ufrgs.inf.pet.dinoapi.communication.google.oauth.GoogleOAuthCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.configuration.properties.GoogleOAuth2Config;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.Event;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.GoogleEvent;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleAPIURLEnum;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.google.calendar.GoogleCalendarModel;
import br.ufrgs.inf.pet.dinoapi.model.google.calendar.GoogleEventDateTimeModel;
import br.ufrgs.inf.pet.dinoapi.model.google.calendar.GoogleEventModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserSettingsServiceImpl;
import br.ufrgs.inf.pet.dinoapi.utils.JsonUtils;
import br.ufrgs.inf.pet.dinoapi.utils.Tuple2;
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


    public GoogleCalendarModel createAndListNewGoogleCalendar(User user) {
        try {
            final GoogleAuth googleAuth = this.getGoogleAuth(user);

            final String accessToken = this.getAccessTokenAndSaveScopes(googleAuth);

            if (accessToken == null) return null;

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

                return JsonUtils.convertJsonToObj(response.body(), GoogleCalendarModel.class);
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            this.logAPIError(e);
        }
        return null;
    }

    private GoogleEventModel makeGoogleEventModel(Event event) {

        String timeZone = ZoneId.SHORT_IDS.get(event.getStart().getZone().getId());

        GoogleEventModel newGoogleEventModel = new GoogleEventModel();
        newGoogleEventModel.setSummary(event.getTitle());
        newGoogleEventModel.setDescription(event.getDescription());

        GoogleEventDateTimeModel start = new GoogleEventDateTimeModel();
        start.setDateTime(new DateTime(event.getStart().toLocalDateTime().toString()));
        start.setTimeZone(timeZone);

        GoogleEventDateTimeModel end = new GoogleEventDateTimeModel();
        end.setDateTime(new DateTime(event.getEnd().toLocalDateTime().toString()));
        end.setTimeZone(timeZone);

        newGoogleEventModel.setStart(start);
        newGoogleEventModel.setEnd(end);

        return newGoogleEventModel;
    }

    private Tuple2<String, String> getAccessTokenAndCalendarId(User user) throws AuthNullException, ConvertModelToEntityException {
        final GoogleAuth googleAuth = this.getGoogleAuth(user);

        final String accessToken = this.getAccessTokenAndSaveScopes(googleAuth);

        if (accessToken == null) return null;

        final String googleCalendarId = user.getUserAppSettings().getGoogleCalendarId();

        if (googleCalendarId == null) return null;

        return new Tuple2<>(accessToken, googleCalendarId);
    }

    public GoogleEventModel insertGoogleEvent(User user, Event event) {
        try {
            Tuple2<String, String> accessTokenAndCalendarId = getAccessTokenAndCalendarId(user);

            if (accessTokenAndCalendarId == null) return null;

            GoogleEventModel newGoogleEventModel = makeGoogleEventModel(event);

            final String jsonModel = JsonUtils.convertToJson(newGoogleEventModel);

            final HttpRequest request = this.createBaseRequest(accessTokenAndCalendarId.getFirst())
                    .uri(new URI(
                            GoogleAPIURLEnum.CALENDARS.getValue()
                                    + "/" + accessTokenAndCalendarId.getSecond()
                                    + "/events"
                                    + "?key="
                                    + googleOAuth2Config.getAPIkey()))
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

    public GoogleEventModel updateGoogleEvent(User user, Event event, GoogleEvent googleEvent) {
        try {
            Tuple2<String, String> accessTokenAndCalendarId = getAccessTokenAndCalendarId(user);

            if (accessTokenAndCalendarId == null) return null;

            GoogleEventModel newGoogleEventModel = makeGoogleEventModel(event);

            final String jsonModel = JsonUtils.convertToJson(newGoogleEventModel);

            final HttpRequest request = this.createBaseRequest(accessTokenAndCalendarId.getFirst())
                    .uri(new URI(
                            GoogleAPIURLEnum.CALENDARS.getValue()
                                    + "/" + accessTokenAndCalendarId.getSecond()
                                    + "/events/"
                                    + googleEvent.getGoogleId()
                                    + "?key=" + googleOAuth2Config.getAPIkey()))
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonModel))
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

    public void deleteGoogleEvent(User user, GoogleEvent googleEvent) {
        try {
            Tuple2<String, String> accessTokenAndCalendarId = getAccessTokenAndCalendarId(user);

            if (accessTokenAndCalendarId == null) return;

            final HttpRequest request = this.createBaseRequest(accessTokenAndCalendarId.getFirst())
                    .uri(new URI(
                            GoogleAPIURLEnum.CALENDARS.getValue()
                                    + "/" + accessTokenAndCalendarId.getSecond()
                                    + "/events/"
                                    + googleEvent.getGoogleId()
                                    + "?key=" + googleOAuth2Config.getAPIkey()))
                    .DELETE().build();

            this.send(request);

        } catch (URISyntaxException | IOException | InterruptedException e) {
            this.logAPIError(e);
        }
    }
}
