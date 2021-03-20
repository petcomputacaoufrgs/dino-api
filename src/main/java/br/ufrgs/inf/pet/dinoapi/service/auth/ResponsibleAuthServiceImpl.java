package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.configuration.properties.ResponsibleAuthConfig;
import br.ufrgs.inf.pet.dinoapi.constants.ResponsibleAuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.responsible.RecoverPasswordRequest;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.exception.ResponsibleRecoverRequestMaxAttemptsException;
import br.ufrgs.inf.pet.dinoapi.exception.ResponsibleRecoverRequestNotFoundException;
import br.ufrgs.inf.pet.dinoapi.language.BaseLanguage;
import br.ufrgs.inf.pet.dinoapi.model.auth.responsible.*;
import br.ufrgs.inf.pet.dinoapi.repository.user.RecoverPasswordRequestRepository;
import br.ufrgs.inf.pet.dinoapi.service.email.EmailServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.language.LanguageServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import br.ufrgs.inf.pet.dinoapi.utils.AESUtils;
import br.ufrgs.inf.pet.dinoapi.utils.AlphaNumericCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResponsibleAuthServiceImpl extends LogUtilsBase implements ResponsibleAuthService {
    private final OAuthServiceImpl authService;
    private final LanguageServiceImpl languageService;
    private final ResponsibleAuthConfig responsibleAuthConfig;
    private final EmailServiceImpl emailService;
    private final RecoverPasswordRequestRepository repository;
    private final UserServiceImpl userService;

    @Autowired
    public ResponsibleAuthServiceImpl(OAuthServiceImpl authService, LanguageServiceImpl languageService,
                                      ResponsibleAuthConfig responsibleAuthConfig, EmailServiceImpl emailService,
                                      LogAPIErrorServiceImpl logAPIErrorService, RecoverPasswordRequestRepository repository,
                                      UserServiceImpl userService) {
        super(logAPIErrorService);
        this.authService = authService;
        this.languageService = languageService;
        this.responsibleAuthConfig = responsibleAuthConfig;
        this.emailService = emailService;
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<ResponsibleRequestRecoverResponseModel> requestRecoverCode() {
        final Auth auth = this.authService.getCurrentAuth();
        final ResponsibleRequestRecoverResponseModel model = new ResponsibleRequestRecoverResponseModel();
        if (auth != null) {
            final BaseLanguage language = languageService.getUserLanguage();
            if (language != null) {
                final LocalDateTime now = LocalDateTime.now();
                final User user = auth.getUser();
                final List<RecoverPasswordRequest> requests = this.repository.findAllByUserId(user.getId());
                final String code = AlphaNumericCodeUtils.generateRandomCode(responsibleAuthConfig.getRecoverCodeLength(), true);

                if (requests.stream().anyMatch(request -> request.getDate().isAfter(now.minusMinutes(ResponsibleAuthConstants.MIN_DELAY_TO_REQUEST_CODE_MIN)))) {
                    model.setSuccess(true);
                    return new ResponseEntity<>(model, HttpStatus.OK);
                }

                this.repository.deleteAll(requests);

                final RecoverPasswordRequest newRequest = new RecoverPasswordRequest();
                newRequest.setCode(code);
                newRequest.setDate(now);
                newRequest.setUser(user);
                newRequest.setAttempts(0);

                this.repository.save(newRequest);

                final String userEmail = user.getEmail();
                final String htmlMessage = this.getHTMLRecoverMessage(newRequest.getCode());

                emailService.sendEmail(userEmail, language.getRecoverPasswordSubject(), htmlMessage);

                model.setSuccess(true);
                return new ResponseEntity<>(model, HttpStatus.OK);
            }

            this.logAPIError("User without language trying to send email.");
            model.setSuccess(false);
            return new ResponseEntity<>(model, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity<ResponsibleVerityRecoverCodeResponseModel> verifyRecoverCode(VerifyResponsibleRecoverCodeModel model) {
        final ResponsibleVerityRecoverCodeResponseModel responseModel =  new ResponsibleVerityRecoverCodeResponseModel();
        final Auth auth = this.authService.getCurrentAuth();
        if (auth != null) {
            try {
                if (this.validateRecoverCode(auth, model.getCode())) {
                    responseModel.setValid(true);
                    return new ResponseEntity<>(responseModel, HttpStatus.OK);
                }
                responseModel.setValid(false);
                return new ResponseEntity<>(responseModel, HttpStatus.OK);
            } catch (ResponsibleRecoverRequestNotFoundException | ResponsibleRecoverRequestMaxAttemptsException e) {
                responseModel.setRequestNotFound(true);
                return new ResponseEntity<>(responseModel, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(responseModel, HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity<SetResponsibleAuthResponseModel> recoverAuth(ResponsibleRecoverPasswordModel model) {
        final SetResponsibleAuthResponseModel responseModel = new SetResponsibleAuthResponseModel();
        final Auth auth = this.authService.getCurrentAuth();
        if (auth != null) {
            final User user = auth.getUser();
            final List<RecoverPasswordRequest> requests = this.repository.findAllByUserIdOrderByDate(user.getId());

            try {
                if (this.validateRecoverCode(auth, model.getCode())) {
                    repository.deleteAll(requests);
                    return this.setResponsibleAuth(responseModel, model, auth);
                }
            } catch (ResponsibleRecoverRequestNotFoundException | ResponsibleRecoverRequestMaxAttemptsException ignored) { }
            responseModel.setSuccess(false);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        }
        responseModel.setSuccess(false);
        return new ResponseEntity<>(responseModel, HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity<SetResponsibleAuthResponseModel> changeAuth(SetResponsibleAuthModel model) {
        final SetResponsibleAuthResponseModel responseModel = new SetResponsibleAuthResponseModel();
        final Auth auth = this.authService.getCurrentAuth();
        if (auth != null) {
            return this.setResponsibleAuth(responseModel, model, auth);
        }
        responseModel.setSuccess(false);
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity<SetResponsibleAuthResponseModel> createAuth(SetResponsibleAuthModel model) {
        final SetResponsibleAuthResponseModel responseModel = new SetResponsibleAuthResponseModel();
        final Auth auth = this.authService.getCurrentAuth();
        if(auth != null) {
            final UserSettings settings = auth.getUser().getUserAppSettings();

            if (settings != null && !settings.getFirstSettingsDone()) {
                return this.setResponsibleAuth(responseModel, model, auth);
            }
        }

        responseModel.setSuccess(false);
        return new ResponseEntity<>(responseModel, HttpStatus.FORBIDDEN);
    }

    @Override
    public void deleteOldData(LocalDateTime deadline) {
        this.repository.deleteAllByDate(deadline);
    }

    private ResponseEntity<SetResponsibleAuthResponseModel> setResponsibleAuth(SetResponsibleAuthResponseModel responseModel, SetResponsibleAuthModel model, Auth auth) {
        try {
            final User user = auth.getUser();
            final String key = model.getKey();
            final String code = AlphaNumericCodeUtils.generateRandomCode(ResponsibleAuthConstants.RESPONSIBLE_CODE_LENGTH, false);
            final String iv = AlphaNumericCodeUtils.generateRandomCode(16, false);

            final Key aesKey = AESUtils.generateAESKey(key);
            final String hash = AESUtils.encrypt(code, aesKey, iv);

            user.setResponsibleIV(iv);
            user.setResponsibleCode(code);
            user.setResponsibleToken(hash);

            this.userService.saveByAuth(user, auth);

            responseModel.setSuccess(true);
            responseModel.setToken(hash);
            responseModel.setIv(iv);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (Exception e) {
            this.logAPIError(e);
            responseModel.setSuccess(false);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        }
    }

    private Boolean validateRecoverCode(Auth auth, String code) throws ResponsibleRecoverRequestNotFoundException, ResponsibleRecoverRequestMaxAttemptsException {
        final LocalDateTime now = LocalDateTime.now();
        final User user = auth.getUser();
        final List<RecoverPasswordRequest> requests = this.repository.findAllByUserIdOrderByDate(user.getId());

        if (requests.size() > 0) {
            final RecoverPasswordRequest request = requests.get(0);
            final Integer maxDelayToRecover = this.responsibleAuthConfig.getMaxDelayToRecoverPasswordInMin();
            if (request.getDate().plusMinutes(maxDelayToRecover).isAfter(now)) {
                if (request.getCode().equals(code)) {
                    request.setAttempts(ResponsibleAuthConstants.MAX_ATTEMPTS - 1);
                    repository.save(request);
                    return true;
                } else {
                    request.setAttempts(request.getAttempts() + 1);

                    if (request.getAttempts() >= ResponsibleAuthConstants.MAX_ATTEMPTS) {
                        repository.deleteAll(requests);
                        throw new ResponsibleRecoverRequestMaxAttemptsException();
                    } else {
                        repository.save(request);
                    }

                    return false;
                }
            }
            repository.deleteAll(requests);
        }

        throw new ResponsibleRecoverRequestNotFoundException();
    }

    private String getHTMLRecoverMessage(String code) {
        return "<div style=\"width: 80%;height: 32rem;font-family: Arial, Helvetica, sans-serif;padding: 2rem;background-color: #694E73;color: #F5F5F5;font-size: 20px;\">\n" +
                "  <h1 style=\"color: #F1D881;font-size: 36px;\">Olá,</h1>\n" +
                "  <p style=\"color: #F5F5F5;\"> Recebemos uma solicitação de alteração de senha de acesso para a área dos responsáveis. Se não tiver solicitado a alteração de senha, basta ignorar esse email e sua senha atual será mantida. </p>\n" +
                "  <div style=\"display: block;\">\n" +
                "    <div style=\"float: left;width: 20rem;height: 20rem;margin-right: 10rem;\">\n" +
                "      <p> O seu código é </p>\n" +
                "      <p style=\"text-align: center; color: #F1D881;margin: 0;width: 16rem;padding: 1rem;background-color: #9D83A7;box-shadow: inset 0px 4px 4px rgba(0, 0, 0, 0.25);border-radius: 0.7rem;font-size: 68px;\"> " + code +" </p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div>";
    }
}
