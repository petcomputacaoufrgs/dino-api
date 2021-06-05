package br.ufrgs.inf.pet.dinoapi.configuration;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.security.DinoGrantedAuthority;
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
        user.setPermission(PermissionEnum.USER.getValue());

        final UserSettings userSettings = new UserSettings();
        userSettings.setStep(0);
        userSettings.setFontSize(0);
        userSettings.setColorTheme(0);
        userSettings.setLanguage(0);
        userSettings.setIncludeEssentialContact(false);
        userSettings.setDeclineGoogleContacts(true);
        userSettings.setFirstSettingsDone(true);
        userSettings.setLastUpdate(LocalDateTime.now());
        userSettings.setUser(user);

        user.setUserAppSettings(userSettings);

        final DinoGrantedAuthority dinoAuthority = new DinoGrantedAuthority(user.getPermission());

        DinoUser basicUser = new DinoUser(user, Collections.singletonList(dinoAuthority));

        return new InMemoryUserDetailsManager(Collections.singletonList(basicUser));
    }
}
