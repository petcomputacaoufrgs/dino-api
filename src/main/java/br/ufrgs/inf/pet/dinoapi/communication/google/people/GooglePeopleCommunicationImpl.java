package br.ufrgs.inf.pet.dinoapi.communication.google.people;

import br.ufrgs.inf.pet.dinoapi.communication.google.oauth.GoogleOAuthCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.enumerable.*;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.google.people.GooglePeopleBiographiesModel;
import br.ufrgs.inf.pet.dinoapi.model.google.people.GooglePeopleModel;
import br.ufrgs.inf.pet.dinoapi.model.google.people.GooglePeopleNameModel;
import br.ufrgs.inf.pet.dinoapi.model.google.people.GooglePeoplePhoneNumberModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleOAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleScopeServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import br.ufrgs.inf.pet.dinoapi.utils.JsonUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GooglePeopleCommunicationImpl extends LogUtilsBase implements GooglePeopleCommunication  {
    private final GoogleOAuthCommunicationImpl googleOAuthCommunication;
    private final GoogleScopeServiceImpl googleScopeService;
    private final GoogleOAuthServiceImpl googleAuthService;

    @Autowired
    public GooglePeopleCommunicationImpl(GoogleOAuthCommunicationImpl googleOAuthCommunication,
                                         GoogleScopeServiceImpl googleScopeService,
                                         GoogleOAuthServiceImpl googleAuthService,
                                         LogAPIErrorServiceImpl logAPIErrorService) {
        super(logAPIErrorService);
        this.googleOAuthCommunication = googleOAuthCommunication;
        this.googleScopeService = googleScopeService;
        this.googleAuthService = googleAuthService;
    }

    @Override
    public GooglePeopleModel getContact(User user, String resourceName) throws IOException, InterruptedException, URISyntaxException {
        final GoogleAuth googleAuth = this.validateGrantsAndGetGoogleAuth(user);

        if (googleAuth == null) return null;

        final String accessToken = this.validateGrantAndGetAccessToken(googleAuth);

        if (accessToken == null) return null;

        if (resourceName == null) return null;

        final HttpRequest request = this.createBaseRequest(accessToken)
                .uri(new URI(
                        GoogleAPIURLEnum.GET_CONTACT_BASE.getValue()
                                + resourceName
                                + "?personFields=names,phoneNumbers,biographies"))
                .GET()
                .build();

        final HttpResponse<String> response = this.send(request);

        if (response.statusCode() == HttpStatus.OK.value()) {
            return JsonUtils.convertJsonToObj(response.body(), GooglePeopleModel.class);
        } else {
            return null;
        }
    }

    @Override
    public GooglePeopleModel createContact(User user, String name, String description, List<String> phoneNumbers) {
        try {
            final GoogleAuth googleAuth = this.validateGrantsAndGetGoogleAuth(user);

            if (googleAuth == null) return null;

            final String accessToken = this.validateGrantAndGetAccessToken(googleAuth);

            if (accessToken == null) return null;

            return this.createNewGoogleContact(accessToken, name, description, phoneNumbers);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            this.logAPIError(e);
        }

        return null;
    }

    @Override
    public GooglePeopleModel updateContact(User user, String name, String description, List<String> phoneNumbers, String resourceName) {
        try {
            final GoogleAuth googleAuth = this.validateGrantsAndGetGoogleAuth(user);

            if (googleAuth == null) return null;

            final String accessToken = this.validateGrantAndGetAccessToken(googleAuth);

            if (accessToken == null) return null;

            final GooglePeopleModel currentPeopleModel = this.getContact(user, resourceName);

            if (currentPeopleModel == null) {
                return this.createNewGoogleContact(accessToken, name, description, phoneNumbers);
            }

            final GooglePeopleModel newGooglePeopleModel = this.getGooglePeopleModel(name, description, phoneNumbers);
            newGooglePeopleModel.setEtag(currentPeopleModel.getEtag());

            final String jsonModel = JsonUtils.convertToJson(newGooglePeopleModel);

            final HttpRequest request = this.createBaseRequest(accessToken)
                    .uri(new URI(
                    GoogleAPIURLEnum.UPDATE_CONTACT_BASE.getValue()
                            + resourceName
                            + ":updateContact?updatePersonFields=names,phoneNumbers,biographies"))
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonModel))
                    .build();

            final HttpResponse<String> response = this.send(request);

            if (response.statusCode() == HttpStatus.OK.value()) {
                return JsonUtils.convertJsonToObj(response.body(), GooglePeopleModel.class);
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            this.logAPIError(e);
        }

        return null;
    }

    @Override
    public boolean deleteContact(User user, GoogleContact googleContact) {
        try {
            final GoogleAuth googleAuth = this.validateGrantsAndGetGoogleAuth(user);

            if (googleAuth == null) return false;

            final String accessToken = this.validateGrantAndGetAccessToken(googleAuth);

            if (accessToken == null) return false;

            final HttpRequest request = this.createBaseRequest(accessToken)
                    .uri(new URI(
                            GoogleAPIURLEnum.DELETE_CONTACT_BASE.getValue()
                                    + googleContact.getResourceName()
                                    + ":deleteContact"))
                    .DELETE()
                    .build();

            this.send(request);

            return true;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            this.logAPIError(e);
        }
        return false;
    }

    private GooglePeopleModel createNewGoogleContact(String accessToken, String name, String description, List<String> phoneNumbers) throws IOException, InterruptedException, URISyntaxException {
        final GooglePeopleModel googlePeopleModel = this.getGooglePeopleModel(name, description, phoneNumbers);

        final String jsonModel = JsonUtils.convertToJson(googlePeopleModel);

        final HttpRequest request = this.createBaseRequest(accessToken)
                .uri(new URI(GoogleAPIURLEnum.CREATE_CONTACT.getValue()))
                .POST(HttpRequest.BodyPublishers.ofString(jsonModel))
                .build();

        final HttpResponse<String> response = this.send(request);

        if (response.statusCode() == HttpStatus.OK.value()) {
            return JsonUtils.convertJsonToObj(response.body(), GooglePeopleModel.class);
        }

        return null;
    }

    private HttpRequest.Builder createBaseRequest(String accessToken) {
        return HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .header(GoogleAPIHeaderEnum.AUTHORIZATION.getValue(), "Bearer " + accessToken)
                .header(HttpHeaderEnum.CONTENT_TYPE.getValue(), HttpContentTypeEnum.JSON.getValue());
    }

    private HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        return HttpClient
                .newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    private GooglePeopleModel getGooglePeopleModel(String name, String description, List<String> phoneNumbers) {
        final GooglePeopleModel model = new GooglePeopleModel();

        final GooglePeopleNameModel nameModel = new GooglePeopleNameModel();
        nameModel.setGivenName(name);

        model.setNames(Collections.singletonList(nameModel));

        if (phoneNumbers.size() > 0) {
            model.setPhoneNumbers(phoneNumbers.stream().map(phoneNumber -> {
                final GooglePeoplePhoneNumberModel numberModel = new GooglePeoplePhoneNumberModel();
                numberModel.setValue(phoneNumber);
                return numberModel;
            }).collect(Collectors.toList()));
        }

        if (description != null) {
            final GooglePeopleBiographiesModel biographiesModel = new GooglePeopleBiographiesModel();
            biographiesModel.setValue(description);
            biographiesModel.setContentType("TEXT_PLAIN");
            model.setBiographies(Collections.singletonList(biographiesModel));
        }

        return model;
    }

    private String validateGrantAndGetAccessToken(GoogleAuth googleAuth) throws AuthNullException, ConvertModelToEntityException {
        if (googleAuth.getAccessToken() == null || LocalDateTime.now().isAfter(googleAuth.getAccessTokenExpiresDate())) {
            final GoogleTokenResponse googleTokenResponse =
                    googleOAuthCommunication.getNewAccessTokenWithRefreshToken(googleAuth);

            if (googleTokenResponse == null) return null;

            final String accessToken = googleTokenResponse.getAccessToken();
            final LocalDateTime expiresDate = googleAuthService.getExpiresDateFromToken(googleTokenResponse);
            googleAuth.setAccessToken(accessToken);
            googleAuth.setAccessTokenExpiresDate(expiresDate);
            googleAuthService.save(googleAuth);

            final boolean stillHasContactGrant = this.saveAllScopes(googleTokenResponse, googleAuth.getUser());

            if (!stillHasContactGrant) return null;

            return accessToken;
        }

        return googleAuth.getAccessToken();
    }

    private GoogleAuth validateGrantsAndGetGoogleAuth(User user) {
        final GoogleAuth googleAuth = user.getGoogleAuth();

        if (googleAuth == null) return null;
        if (googleAuth.getRefreshToken() == null) return null;

        final UserSettings userSettings = user.getUserAppSettings();

        if (userSettings.getDeclineGoogleContacts()) return null;

        final boolean hasContactGrant =
                googleScopeService.hasGoogleContactScope(user);

        if (!hasContactGrant) return null;

        return googleAuth;
    }

    private boolean saveAllScopes(GoogleTokenResponse googleTokenResponse, User user) throws AuthNullException, ConvertModelToEntityException {
        final List<String> currentScopes = Arrays.asList(googleTokenResponse.getScope().split(" "));

        final Auth fakeAuth = new Auth();
        fakeAuth.setUser(user);

        googleAuthService.saveAllScopes(currentScopes, fakeAuth);

        final Optional<String> googleContactScope = currentScopes.stream().filter(scope -> scope.equals(GoogleScopeURLEnum.SCOPE_CONTACT.getValue())).findFirst();

        return googleContactScope.isPresent();
    }
}
