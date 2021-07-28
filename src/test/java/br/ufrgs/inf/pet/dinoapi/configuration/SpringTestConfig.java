package br.ufrgs.inf.pet.dinoapi.configuration;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.security.DinoGrantedAuthority;
import br.ufrgs.inf.pet.dinoapi.security.DinoUserDetails;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TestConfiguration
public class SpringTestConfig {
    @Primary
    @Bean()
    public UserDetailsService userDetailsService() {
        final List<UserDetails> userDetails = new ArrayList<>();
        userDetails.add(getBasicUserDetails());
        userDetails.add(getAdminUserDetails());
        userDetails.add(getStaffUserDetails());

        return new InMemoryUserDetailsManager(userDetails);
    }

    private DinoUserDetails getBasicUserDetails() {
        final User user = new User();
        user.setName("user");
        user.setEmail("user@dinoapp.com");
        user.setPermission(PermissionEnum.USER.getValue());

        final DinoGrantedAuthority dinoAuthority = new DinoGrantedAuthority(user.getPermission());

        return new DinoUserDetails(user, Collections.singletonList(dinoAuthority));
    }

    private DinoUserDetails getAdminUserDetails() {
        final User user = new User();
        user.setName("admin");
        user.setEmail("admin@dinoapp.com");
        user.setPermission(PermissionEnum.ADMIN.getValue());

        final DinoGrantedAuthority dinoAuthority = new DinoGrantedAuthority(user.getPermission());

        return new DinoUserDetails(user, Collections.singletonList(dinoAuthority));
    }

    private DinoUserDetails getStaffUserDetails() {
        final User user = new User();
        user.setName("staff");
        user.setEmail("staff@dinoapp.com");
        user.setPermission(PermissionEnum.STAFF.getValue());

        final DinoGrantedAuthority dinoAuthority = new DinoGrantedAuthority(user.getPermission());

        return new DinoUserDetails(user, Collections.singletonList(dinoAuthority));
    }
}
