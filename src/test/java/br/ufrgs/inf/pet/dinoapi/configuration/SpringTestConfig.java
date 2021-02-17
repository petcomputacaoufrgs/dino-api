package br.ufrgs.inf.pet.dinoapi.configuration;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.security.DinoUser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import java.time.LocalDateTime;
import java.util.Collections;

@TestConfiguration
public class SpringTestConfig {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        final User user = new User();
        user.setName("Basic user");
        user.setEmail("basicuser@dinoapp.com");
        user.setLastUpdate(LocalDateTime.now());

        final UserSettings userSettings = new UserSettings();
        userSettings.setSettingsStep(0);
        userSettings.setFontSize(0);
        userSettings.setColorTheme(0);
        userSettings.setLanguage(0);
        userSettings.setIncludeEssentialContact(false);
        userSettings.setDeclineGoogleContacts(true);
        userSettings.setFirstSettingsDone(true);
        userSettings.setLastUpdate(LocalDateTime.now());
        userSettings.setUser(user);

        user.setUserAppSettings(userSettings);

        DinoUser basicUser = new DinoUser(user);

        return new InMemoryUserDetailsManager(Collections.singletonList(basicUser));
    }
}
