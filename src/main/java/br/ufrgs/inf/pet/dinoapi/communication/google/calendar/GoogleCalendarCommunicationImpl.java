package br.ufrgs.inf.pet.dinoapi.communication.google.calendar;

import br.ufrgs.inf.pet.dinoapi.communication.google.GoogleCommunication;
import br.ufrgs.inf.pet.dinoapi.configuration.properties.GoogleOAuth2Config;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleAPIURLEnum;
import br.ufrgs.inf.pet.dinoapi.model.calendar.GoogleCalendarModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserSettingsServiceImpl;
import br.ufrgs.inf.pet.dinoapi.utils.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GoogleCalendarCommunicationImpl extends GoogleCommunication {

    final UserSettingsServiceImpl userSettingsService;
    final AuthServiceImpl authService;
    final GoogleOAuth2Config googleOAuth2Config;

    public GoogleCalendarCommunicationImpl(LogAPIErrorServiceImpl logAPIErrorService,
                                           AuthServiceImpl authService,
                                           UserSettingsServiceImpl userSettingsService,
                                           GoogleOAuth2Config googleOAuth2Config) {
        super(logAPIErrorService);
        this.userSettingsService = userSettingsService;
        this.authService = authService;
        this.googleOAuth2Config = googleOAuth2Config;

    }

    public GoogleCalendarModel createAndListNewGoogleCalendar() {
        try {

            String accessToken = authService.getCurrentAuth().getAccessToken();

            if(accessToken == null) return null;

            GoogleCalendarModel newGoogleCalendarModel = new GoogleCalendarModel("Test DinoApp Calendar");

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

                final UserSettings settings = authService.getCurrentAuth().getUser().getUserAppSettings();
                settings.setGoogleCalendarId(createdCalendar.getId());
                userSettingsService.saveDirectly(settings);

                return listNewGoogleCalendar(createdCalendar, accessToken);
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            this.logAPIError(e);
        }

        return null;
    }

    private GoogleCalendarModel listNewGoogleCalendar (GoogleCalendarModel model, String accessToken
    ) {
        try {
            final String jsonModel = JsonUtils.convertToJson(model);

            final HttpRequest request = this.createBaseRequest(accessToken)
                    .uri(new URI(
                            GoogleAPIURLEnum.CALENDAR_LIST.getValue()
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

}
