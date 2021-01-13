package br.ufrgs.inf.pet.dinoapi.communication.google.people;

import br.ufrgs.inf.pet.dinoapi.communication.google.oauth.GoogleaOAuthCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.enumerable.*;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactDataModel;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GooglePeopleCommunicationImpl extends LogUtilsBase implements GooglePeopleCommunication  {
    private final GoogleaOAuthCommunicationImpl googleOAuthCommunication;
    private final GoogleScopeServiceImpl googleScopeService;
    private final GoogleOAuthServiceImpl googleAuthService;

    @Autowired
    public GooglePeopleCommunicationImpl(GoogleaOAuthCommunicationImpl googleOAuthCommunication,
                                         GoogleScopeServiceImpl googleScopeService,
                                         GoogleOAuthServiceImpl googleAuthService,
                                         LogAPIErrorServiceImpl logAPIErrorService) {
        super(logAPIErrorService);
        this.googleOAuthCommunication = googleOAuthCommunication;
        this.googleScopeService = googleScopeService;
        this.googleAuthService = googleAuthService;
    }

    @Override
    public GooglePeopleModel getContact(User user, GoogleContact googleContact) {
        try {
            final GoogleAuth googleAuth = this.validateGrantsAndGetGoogleAuth(user);

            if (googleAuth == null) return null;

            final String accessToken = this.validateGrantAndGetAccessToken(googleAuth);

            if (accessToken == null) return null;

            if (googleContact.getResourceName() == null) return null;

            final HttpRequest request = this.createBaseRequest(accessToken)
                    .uri(new URI(
                            GoogleAPIURLEnum.GET_CONTACT_BASE.getValue()
                                    + googleContact.getResourceName()
                                    + "?personFields=names,phoneNumbers,biographies"))
                    .GET()
                    .build();

            final HttpResponse<String> response = this.send(request);

            if (response.statusCode() == HttpStatus.OK.value()) {
                return JsonUtils.convertJsonToObj(response.body(), GooglePeopleModel.class);
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            this.logAPIError(e.getMessage());
        }

        return null;
    }

    @Override
    public GooglePeopleModel createContact(User user, ContactDataModel contactDataModel) {
        try {
            final GoogleAuth googleAuth = this.validateGrantsAndGetGoogleAuth(user);

            if (googleAuth == null) return null;

            final String accessToken = this.validateGrantAndGetAccessToken(googleAuth);

            if (accessToken == null) return null;

            final GooglePeopleModel googlePeopleModel = this.getGooglePeopleModel(contactDataModel, new ArrayList<>());

            final String jsonModel = JsonUtils.convertToJson(googlePeopleModel);

            final HttpRequest request = this.createBaseRequest(accessToken)
                    .uri(new URI(GoogleAPIURLEnum.CREATE_CONTACT.getValue()))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonModel))
                    .build();

            final HttpResponse<String> response = this.send(request);

            if (response.statusCode() == HttpStatus.OK.value()) {
                return JsonUtils.convertJsonToObj(response.body(), GooglePeopleModel.class);
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            this.logAPIError(e.getMessage());
        }

        return null;
    }

    @Override
    public GooglePeopleModel updateContact(User user, ContactDataModel contactDataModel, List<Phone> phones, GoogleContact googleContact) {
        try {
            final GoogleAuth googleAuth = this.validateGrantsAndGetGoogleAuth(user);

            if (googleAuth == null) return null;

            final String accessToken = this.validateGrantAndGetAccessToken(googleAuth);

            if (accessToken == null) return null;

            final GooglePeopleModel currentPeopleModel = this.getContact(user, googleContact);

            final GooglePeopleModel newGooglePeopleModel = this.getGooglePeopleModel(contactDataModel, phones);
            newGooglePeopleModel.setEtag(currentPeopleModel.getEtag());

            final String jsonModel = JsonUtils.convertToJson(newGooglePeopleModel);

            final HttpRequest request = this.createBaseRequest(accessToken)
                    .uri(new URI(
                    GoogleAPIURLEnum.UPDATE_CONTACT_BASE.getValue()
                            + googleContact.getResourceName()
                            + "/:updateContact?updatePersonFields=names,phoneNumbers,biographies"))
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonModel))
                    .build();

            final HttpResponse<String> response = this.send(request);

            if (response.statusCode() == HttpStatus.OK.value()) {
                return JsonUtils.convertJsonToObj(response.body(), GooglePeopleModel.class);
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            this.logAPIError(e.getMessage());
        }

        return null;
    }

    @Override
    public void deleteContact(User user, ContactDataModel contactDataModel, List<Phone> phones, GoogleContact googleContact) {
        try {
            final GoogleAuth googleAuth = this.validateGrantsAndGetGoogleAuth(user);

            if (googleAuth == null) return;

            final String accessToken = this.validateGrantAndGetAccessToken(googleAuth);

            if (accessToken == null) return;

            final HttpRequest request = this.createBaseRequest(accessToken)
                    .uri(new URI(
                            GoogleAPIURLEnum.DELETE_CONTACT_BASE.getValue()
                                    + googleContact.getResourceName()
                                    + "/:deleteContact"))
                    .DELETE()
                    .build();

            this.send(request);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            this.logAPIError(e.getMessage());
        }
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

    private GooglePeopleModel getGooglePeopleModel(ContactDataModel contactDataModel, List<Phone> phones) {
        final GooglePeopleModel model = new GooglePeopleModel();

        final GooglePeopleNameModel nameModel = new GooglePeopleNameModel();
        nameModel.setGivenName(contactDataModel.getName());

        model.setNames(Collections.singletonList(nameModel));

        if (phones.size() > 0) {
            model.setPhoneNumbers(phones.stream().map(phone -> {
                final GooglePeoplePhoneNumberModel numberModel = new GooglePeoplePhoneNumberModel();
                numberModel.setValue(phone.getNumber());
                return numberModel;
            }).collect(Collectors.toList()));
        }

        if (contactDataModel.getDescription() != null) {
            final GooglePeopleBiographiesModel biographiesModel = new GooglePeopleBiographiesModel();
            biographiesModel.setValue(contactDataModel.getDescription());
            biographiesModel.setContentType("TEXT_PLAIN");
            model.setBiographies(Collections.singletonList(biographiesModel));
        }

        return model;
    }

    private String validateGrantAndGetAccessToken(GoogleAuth googleAuth) throws AuthNullException, ConvertModelToEntityException {
        final GoogleTokenResponse googleTokenResponse =
                googleOAuthCommunication.getNewAccessTokenWithRefreshToken(googleAuth.getRefreshToken());

        final String accessToken = googleTokenResponse.getAccessToken();

        final boolean stillHasContactGrant = this.saveAllScopes(googleTokenResponse, googleAuth.getUser());

        if (!stillHasContactGrant) return null;

        return accessToken;
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
