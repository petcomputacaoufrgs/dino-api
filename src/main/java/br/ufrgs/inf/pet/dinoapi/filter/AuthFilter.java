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

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    GoogleAuthServiceImpl googleAuthService;

    @Autowired
    DinoUserDetailsService dinoUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        this.startServices(httpServletRequest);

        final String token = this.getAuthToken(httpServletRequest);

        if (token != null) {
            final Auth auth = authService.findByAccessToken(token);

            if (auth != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                this.updateTokensIfNecessary(auth, httpServletResponse);
                UserDetails userDetails = dinoUserDetailsService.loadUserDetailsByAuth(auth);

                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getAuthToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HeaderEnum.AUTHORIZATION.getValue());

        if (token == null) {
            token = httpServletRequest.getParameter(HeaderEnum.AUTHORIZATION.getValue());
        }

        return token;
    }

    private void updateTokensIfNecessary(Auth auth, HttpServletResponse httpServletResponse) throws JsonProcessingException {
        final User user = auth.getUser();

        if (!auth.tokenIsValid()) {
            Auth newAuth = authService.refreshAuth(auth);

            httpServletResponse.setHeader(HeaderEnum.REFRESH.getValue(), newAuth.getAccessToken());
        }

        if(user.hasGoogleAuth()) {
            GoogleAuth googleAuth = user.getGoogleAuth();
            if (!googleAuth.tokenIsValid()) {
                googleAuth = googleAuthService.refreshGoogleAuth(googleAuth);
                final String expiresDate = JsonUtils.convertObjectToJSON(googleAuth.getTokenExpiresDateInMillis());

                httpServletResponse.setHeader(HeaderEnum.GOOGLE_REFRESH.getValue(), googleAuth.getAccessToken());
                httpServletResponse.setHeader(HeaderEnum.GOOGLE_EXPIRES_DATE.getValue(), expiresDate);
            }
        }
    }

    private void startServices(HttpServletRequest httpServletRequest) {
        ServletContext servletContext = null;
        WebApplicationContext webApplicationContext = null;

        if (servletContext == null) {
            servletContext = httpServletRequest.getServletContext();
        }

        if (webApplicationContext == null) {
            webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        }

        if(userService == null){
            userService = webApplicationContext.getBean(UserServiceImpl.class);
        }

        if(authService == null) {
            authService = webApplicationContext.getBean(AuthServiceImpl.class);
        }

        if(googleAuthService == null) {
            googleAuthService = webApplicationContext.getBean(GoogleAuthServiceImpl.class);
        }

        if(dinoUserDetailsService == null){
            dinoUserDetailsService = webApplicationContext.getBean(DinoUserDetailsService.class);
        }
    }
}
