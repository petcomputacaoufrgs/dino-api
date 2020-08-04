package br.ufrgs.inf.pet.dinoapi.filter;

import br.ufrgs.inf.pet.dinoapi.entity.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.HeaderEnum;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user_details.DinoUserDetailsService;
import br.ufrgs.inf.pet.dinoapi.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implementação de um filtro para tratar os dados de autorização da API
 *
 * @author joao.silva
 */
@Component
public class AuthFilter extends OncePerRequestFilter {

    private UserServiceImpl userService;

    private AuthServiceImpl authService;

    private GoogleAuthServiceImpl googleAuthService;

    private DinoUserDetailsService dinoUserDetailsService;

    @Autowired
    public AuthFilter(UserServiceImpl userService, AuthServiceImpl authService, GoogleAuthServiceImpl googleAuthService, DinoUserDetailsService dinoUserDetailsService) {
        super();
        this.userService = userService;
        this.authService = authService;
        this.googleAuthService = googleAuthService;
        this.dinoUserDetailsService = dinoUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        this.startServices(httpServletRequest);

        final String token = this.getAuthToken(httpServletRequest);

        if (token != null) {
            final Auth auth = authService.findByAccessToken(token);

            if (auth != null && this.validUserAgent(auth, httpServletRequest)) {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    this.updateTokensIfNecessary(auth, httpServletRequest, httpServletResponse);
                    UserDetails userDetails = this.dinoUserDetailsService.loadUserDetailsByAuth(auth);

                    final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken); 
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private Boolean validUserAgent(Auth auth, HttpServletRequest httpServletRequest) {
        return auth.getUserAgent().equals(httpServletRequest.getHeader("User-Agent"));
    }

    private String getAuthToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HeaderEnum.AUTHORIZATION.getValue());

        if (token == null) {
            token = httpServletRequest.getParameter(HeaderEnum.AUTHORIZATION.getValue());
        }

        return token;
    }

    private void updateTokensIfNecessary(Auth auth, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws JsonProcessingException {
        final User user = auth.getUser();

        if (!auth.tokenIsValid()) {
            Auth newAuth = this.authService.refreshAuth(auth, httpServletRequest);

            httpServletResponse.setHeader(HeaderEnum.REFRESH.getValue(), newAuth.getAccessToken());
        }

        if(user.hasGoogleAuth()) {
            GoogleAuth googleAuth = user.getGoogleAuth();
            if (!googleAuth.tokenIsValid()) {
                googleAuth = this.googleAuthService.refreshGoogleAuth(googleAuth);
                final String expiresDate = JsonUtils.convertObjectToJSON(googleAuth.getTokenExpiresDateInMillis());

                httpServletResponse.setHeader(HeaderEnum.GOOGLE_REFRESH.getValue(), googleAuth.getAccessToken());
                httpServletResponse.setHeader(HeaderEnum.GOOGLE_EXPIRES_DATE.getValue(), expiresDate);
            }
        }
    }

    ///acorda pra trabalhar
    private void startServices(HttpServletRequest httpServletRequest) {
        ServletContext servletContext = null;
        WebApplicationContext webApplicationContext = null;

        if (servletContext == null) {
            servletContext = httpServletRequest.getServletContext();
        }

        if (webApplicationContext == null) {
            webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        }

        if(this.userService == null){
            this.userService = webApplicationContext.getBean(UserServiceImpl.class);
        }

        if(this.authService == null) {
            this.authService = webApplicationContext.getBean(AuthServiceImpl.class);
        }

        if(this.googleAuthService == null) {
            this.googleAuthService = webApplicationContext.getBean(GoogleAuthServiceImpl.class);
        }

        if(this.dinoUserDetailsService == null){
            this.dinoUserDetailsService = webApplicationContext.getBean(DinoUserDetailsService.class);
        }
    }
}
