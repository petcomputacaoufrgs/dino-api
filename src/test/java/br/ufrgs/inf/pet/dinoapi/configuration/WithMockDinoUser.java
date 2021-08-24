package br.ufrgs.inf.pet.dinoapi.configuration;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.security.DinoAuthenticationToken;
import br.ufrgs.inf.pet.dinoapi.security.DinoCredentials;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.StringUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithDinoSecurityContextFactory.class)
public @interface WithMockDinoUser {
    String email();

    String userDetailsServiceBeanName() default "";

    boolean includeEssentialContact() default false;

    boolean firstSettingsDone() default true;
}

final class WithDinoSecurityContextFactory implements WithSecurityContextFactory<WithMockDinoUser> {
    private final BeanFactory beans;

    @Autowired
    public WithDinoSecurityContextFactory(BeanFactory beans) {
        this.beans = beans;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockDinoUser withMockDinoUser) {
        final String beanName = withMockDinoUser.userDetailsServiceBeanName();

        final UserDetailsService userDetailsService = StringUtils.hasLength(beanName)
                ? this.beans.getBean(beanName, UserDetailsService.class)
                : this.beans.getBean(UserDetailsService.class);

        UserDetails principal = userDetailsService.loadUserByUsername(withMockDinoUser.email());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(principal.getAuthorities());

        final User user = new User();
        user.setName("mock");
        user.setEmail(principal.getUsername());
        user.setLastUpdate(LocalDateTime.now());
        user.setPermission(grantedAuthorities.get(0).getAuthority());

        final UserSettings userSettings = new UserSettings();
        userSettings.setFontSize(0);
        userSettings.setColorTheme(0);
        userSettings.setLanguage(0);
        userSettings.setIncludeEssentialContact(withMockDinoUser.includeEssentialContact());
        userSettings.setDeclineGoogleContacts(true);
        userSettings.setFirstSettingsDone(withMockDinoUser.firstSettingsDone());
        userSettings.setLastUpdate(LocalDateTime.now());
        userSettings.setUser(user);

        user.setUserAppSettings(userSettings);

        final Auth auth = new Auth();
        auth.setUser(user);

        final DinoCredentials dinoCredentials = new DinoCredentials(auth);

        final DinoAuthenticationToken dinoAuthToken = new DinoAuthenticationToken(user, dinoCredentials, grantedAuthorities);

        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(dinoAuthToken);

        return securityContext;
    }
}
