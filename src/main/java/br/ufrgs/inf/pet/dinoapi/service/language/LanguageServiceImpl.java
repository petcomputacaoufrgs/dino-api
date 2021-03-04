package br.ufrgs.inf.pet.dinoapi.service.language;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.enumerable.LanguageEnum;
import br.ufrgs.inf.pet.dinoapi.language.BaseLanguage;
import br.ufrgs.inf.pet.dinoapi.language.ENLanguage;
import br.ufrgs.inf.pet.dinoapi.language.PTLanguage;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageServiceImpl implements LanguageService {
    private final OAuthServiceImpl oAuthService;

    @Autowired
    public LanguageServiceImpl(OAuthServiceImpl oAuthService) {
        this.oAuthService = oAuthService;
    }

    @Override
    public BaseLanguage getUserLanguage() {
        final Auth auth = oAuthService.getCurrentAuth();
        if (auth != null) {
            final UserSettings userSettings = auth.getUser().getUserAppSettings();
            if (userSettings != null) {
                if (userSettings.getLanguage() == LanguageEnum.PORTUGUESE.getValue()) {
                    return new PTLanguage();
                } else if (userSettings.getLanguage() == LanguageEnum.ENGLISH.getValue()) {
                    return new ENLanguage();
                }
            }
        }

        return null;
    }
}
