package br.ufrgs.inf.pet.dinoapi.security;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.enumerable.HeaderEnum;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleOAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Component
public class AuthFilter extends OncePerRequestFilter {

    private UserServiceImpl userService;

    private OAuthServiceImpl authService;

    private GoogleOAuthServiceImpl googleAuthService;

    private DinoUserDetailsService dinoUserDetailsService;

    @Autowired
    public AuthFilter(UserServiceImpl userService, OAuthServiceImpl authService, GoogleOAuthServiceImpl googleAuthService, DinoUserDetailsService dinoUserDetailsService) {
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

            if (auth != null) {
                this.setAuth(httpServletRequest, auth);
            }
        } else {
            final String wsToken = this.getWSToken(httpServletRequest);

            if (wsToken != null) {
                final Auth auth = authService.findByWebSocketToken(wsToken);

                if (auth != null && authService.canConnectToWebSocket(auth)) {
                    this.setAuth(httpServletRequest, auth);
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void setAuth(HttpServletRequest httpServletRequest, Auth auth) {
        if (authService.isValidAccessToken(auth.getAccessToken())) {
            final DinoAuthenticationToken dinoAuthToken = dinoUserDetailsService.loadDinoUserByAuth(auth);
            dinoAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            SecurityContextHolder.getContext().setAuthentication(dinoAuthToken);
        }
    }

    private String getAuthToken(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(HeaderEnum.AUTHORIZATION.getValue());
    }

    private String getWSToken(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter(HeaderEnum.WS_AUTHORIZATION.getValue());
    }

    private void startServices(HttpServletRequest httpServletRequest) {
        final ServletContext servletContext =
                httpServletRequest.getServletContext();
        final WebApplicationContext webApplicationContext =
                WebApplicationContextUtils.getWebApplicationContext(servletContext);

        if (webApplicationContext != null) {
            if (this.userService == null) {
                this.userService = webApplicationContext.getBean(UserServiceImpl.class);
            }

            if (this.authService == null) {
                this.authService = webApplicationContext.getBean(OAuthServiceImpl.class);
            }

            if (this.googleAuthService == null) {
                this.googleAuthService = webApplicationContext.getBean(GoogleOAuthServiceImpl.class);
            }

            if (this.dinoUserDetailsService == null) {
                this.dinoUserDetailsService = webApplicationContext.getBean(DinoUserDetailsService.class);
            }
        }
    }
}
