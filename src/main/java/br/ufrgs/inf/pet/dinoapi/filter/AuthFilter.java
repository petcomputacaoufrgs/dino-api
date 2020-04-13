package br.ufrgs.inf.pet.dinoapi.filter;

import br.ufrgs.inf.pet.dinoapi.entity.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.HeaderEnum;
import br.ufrgs.inf.pet.dinoapi.service.auth.dino.DinoAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user_details.DinoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
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
    DinoAuthServiceImpl dinoAuthService;

    @Autowired
    GoogleAuthServiceImpl googleAuthService;

    @Autowired
    DinoUserDetailsService dinoUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        startServices(httpServletRequest);

        final String authorizationHeader = httpServletRequest.getHeader(HeaderEnum.AUTHORIZATION.getValue());

        if (authorizationHeader != null) {
            final String token = removeTokenType(authorizationHeader);

            if (token != null) {

                User user = userService.findByAccessToken(token);

                if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    updateTokensIfNecessary(user, httpServletResponse);

                    UserDetails userDetails = dinoUserDetailsService.loadUserByUsername(user.getEmail());

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String removeTokenType(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    private void updateTokensIfNecessary(User user, HttpServletResponse httpServletResponse) {
        if (!user.tokenIsValid()) {
            String newToken = dinoAuthService.refreshAccessToken(user);

            httpServletResponse.setHeader(HeaderEnum.REFRESH.getValue(), "Bearer " + newToken);
        }

        if(user.hasGoogleAuth()) {
            GoogleAuth googleAuth = user.getGoogleAuth();
            if (!googleAuth.tokenIsValid()) {
                String newToken = googleAuthService.refreshGoogleAuth(googleAuth);

                httpServletResponse.setHeader(HeaderEnum.GOOGLE_REFRESH.getValue(), "Bearer " + newToken);
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

        if(dinoAuthService == null) {
            dinoAuthService = webApplicationContext.getBean(DinoAuthServiceImpl.class);
        }

        if(googleAuthService == null) {
            googleAuthService = webApplicationContext.getBean(GoogleAuthServiceImpl.class);
        }

        if(dinoUserDetailsService == null){
            dinoUserDetailsService = webApplicationContext.getBean(DinoUserDetailsService.class);
        }
    }
}
