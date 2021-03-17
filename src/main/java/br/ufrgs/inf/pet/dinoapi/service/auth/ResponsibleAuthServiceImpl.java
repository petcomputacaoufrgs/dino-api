package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.configuration.application_properties.RecoverPasswordConfig;
import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.RecoverPasswordRequest;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.language.BaseLanguage;
import br.ufrgs.inf.pet.dinoapi.model.user.*;
import br.ufrgs.inf.pet.dinoapi.repository.user.RecoverPasswordRequestRepository;
import br.ufrgs.inf.pet.dinoapi.service.email.EmailServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.language.LanguageServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import br.ufrgs.inf.pet.dinoapi.utils.AESUtils;
import br.ufrgs.inf.pet.dinoapi.utils.AlphaNumericCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.security.Provider;
import java.security.Security;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponsibleAuthServiceImpl extends LogUtilsBase implements ResponsibleAuthService {
    private static final Short MIN_DELAY_TO_REQUEST_CODE_MIN = 2;
    private static final Short MAX_DELAY_TO_RECOVER_PASSWORD_MIN = 60;
    private static final Short MAX_ATTEMPTS = 3;
    private static final String ENCRYPT_ALGORITHM = "AES/CBC/PKCS5Padding";

    private final OAuthServiceImpl authService;
    private final LanguageServiceImpl languageService;
    private final RecoverPasswordConfig recoverPasswordConfig;
    private final EmailServiceImpl emailService;
    private final RecoverPasswordRequestRepository repository;

    @Autowired
    public ResponsibleAuthServiceImpl(OAuthServiceImpl authService, LanguageServiceImpl languageService,
                                      RecoverPasswordConfig recoverPasswordConfig, EmailServiceImpl emailService,
                                      LogAPIErrorServiceImpl logAPIErrorService, RecoverPasswordRequestRepository repository) {
        super(logAPIErrorService);
        this.authService = authService;
        this.languageService = languageService;
        this.recoverPasswordConfig = recoverPasswordConfig;
        this.emailService = emailService;
        this.repository = repository;
    }

    @Override
    public ResponseEntity<Void> requestRecoverCode() {
        final Auth auth = this.authService.getCurrentAuth();
        if (auth != null) {
            final BaseLanguage language = languageService.getUserLanguage();
            if (language != null) {
                final LocalDateTime now = LocalDateTime.now();
                final User user = auth.getUser();
                final List<RecoverPasswordRequest> requests = this.repository.findAllByUserId(user.getId());
                final String code = AlphaNumericCodeUtils.generateRandomCode(recoverPasswordConfig.getCodeLength(), true);

                if (requests.stream().anyMatch(request -> request.getDate().isAfter(now.minusMinutes(MIN_DELAY_TO_REQUEST_CODE_MIN)))) {
                    return new ResponseEntity<>(HttpStatus.OK);
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

                return new ResponseEntity<>(HttpStatus.OK);
            }

            this.logAPIError("User without language trying to send email.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity<Boolean> verifyRecoverCode(RecoverPasswordDataModel model) {
        final Auth auth = this.authService.getCurrentAuth();
        if (auth != null) {
            if (this.validateRecoverCode(auth, model)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity<CreateResponsibleAuthResponseModel> changeAuth(RecoverPasswordDataModel model) {
        final CreateResponsibleAuthResponseModel responseModel = new CreateResponsibleAuthResponseModel();
        final Auth auth = this.authService.getCurrentAuth();
        if (auth != null) {
            final User user = auth.getUser();
            final List<RecoverPasswordRequest> requests = this.repository.findAllByUserIdOrderByDate(user.getId());

            if (this.validateRecoverCode(auth, model)) {
                //TO-DO Gerar novo token com a nova senha
                auth.setResponsibleHash("TO-DO");
                responseModel.setSuccess(true);
                responseModel.setHash("TO-DO");

                repository.deleteAll(requests);
                return new ResponseEntity<>(responseModel, HttpStatus.OK);
            }
            responseModel.setSuccess(false);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        responseModel.setSuccess(false);
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity<CreateResponsibleAuthResponseModel> createResponsibleAuth(CreateResponsibleAuthModel model) {
        final CreateResponsibleAuthResponseModel responseModel = new CreateResponsibleAuthResponseModel();
        final Auth auth = this.authService.getCurrentAuth();

        if(auth != null) {
            try {
                final String password = model.getPassword();
                final String code = AlphaNumericCodeUtils.generateRandomCode(AuthConstants.RESPONSIBLE_CODE_LENGTH, false);
                final String salt = AlphaNumericCodeUtils.generateRandomCode(password.length(), false);
                final Key key = AESUtils.generateAES32BytesKey(password);

                List<String> algorithms = Arrays.stream(Security.getProviders())
                        .flatMap(provider -> provider.getServices().stream())
                        .filter(service -> "Cipher".equals(service.getType()))
                        .map(Provider.Service::getAlgorithm)
                        .collect(Collectors.toList());

                final String hash = AESUtils.encrypt(ENCRYPT_ALGORITHM, code, key);

                auth.setResponsibleSalt(salt);
                auth.setResponsibleCode(code);
                auth.setResponsibleHash(hash);
                this.authService.save(auth);
                responseModel.setSuccess(true);
                responseModel.setHash(hash);
                responseModel.setSalt(salt);
                return new ResponseEntity<>(responseModel, HttpStatus.OK);
            } catch (Exception e) {
                this.logAPIError(e);
                responseModel.setSuccess(false);
                return new ResponseEntity<>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        responseModel.setSuccess(false);
        return new ResponseEntity<>(responseModel, HttpStatus.FORBIDDEN);
    }

    private Boolean validateRecoverCode(Auth auth, RecoverPasswordDataModel model) {
        final LocalDateTime now = LocalDateTime.now();
        final User user = auth.getUser();
        final List<RecoverPasswordRequest> requests = this.repository.findAllByUserIdOrderByDate(user.getId());

        if (requests.size() > 0) {
            final RecoverPasswordRequest request = requests.get(0);
            if (request.getDate().plusMinutes(MAX_DELAY_TO_RECOVER_PASSWORD_MIN).isAfter(now)) {
                if (request.getCode().equals(model.getCode())) {
                    return true;
                } else {
                    request.setAttempts(request.getAttempts() + 1);

                    if (request.getAttempts() >= MAX_ATTEMPTS * 2) {
                        repository.deleteAll(requests);
                    }

                    return false;
                }
            }
            repository.deleteAll(requests);
        }

        return false;
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
