package br.ufrgs.inf.pet.dinoapi.configuration;

import br.ufrgs.inf.pet.dinoapi.configuration.properties.AppConfig;
import br.ufrgs.inf.pet.dinoapi.enumerable.HeaderEnum;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.security.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.concurrent.Executor;

@Configuration
@EnableWebSecurity
@EnableAsync(proxyTargetClass = true)
@EnableScheduling
public class SpringConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService dinoUserDetailsService;

    private final AuthFilter authFilter;

    private final AppConfig appConfig;

    @Autowired
    public SpringConfig(AuthFilter authFilter, UserDetailsService dinoUserDetailsService,
                        AppConfig appConfig) {
        super();
        this.authFilter = authFilter;
        this.dinoUserDetailsService = dinoUserDetailsService;
        this.appConfig = appConfig;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.dinoUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        final String adminAuthority = PermissionEnum.ADMIN.getValue();
        final String staffAuthority = PermissionEnum.STAFF.getValue();
        final String userAuthority = PermissionEnum.USER.getValue();
        httpSecurity.authorizeRequests()
                .antMatchers("/google1da5cc70ff16112c.html").permitAll()
                .antMatchers("/staff/**").hasAnyAuthority(staffAuthority, adminAuthority)
                .antMatchers("/user/**").hasAuthority(userAuthority)
                .antMatchers("/private/**").authenticated()
                .antMatchers("/public/**").permitAll()
                .and().cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(this.authFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(appConfig.getOrigin()));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setExposedHeaders(Collections.singletonList(
                HeaderEnum.AUTHORIZATION.getValue()));
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean(name = "contactThreadPoolTaskExecutor")
    public Executor contactThreadPoolTaskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(Integer.MAX_VALUE);
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.setThreadNamePrefix("dino-api-contact-thread-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "defaultThreadPoolTaskExecutor")
    public Executor defaultThreadPoolTaskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.setThreadNamePrefix("dino-api-default-thread-");
        executor.initialize();
        return executor;
    }
}
