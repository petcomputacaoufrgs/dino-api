package br.ufrgs.inf.pet.dinoapi.filter;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
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
    AuthServiceImpl authService;

    @Autowired
    DinoUserDetailsService dinoUserDetailsService;

    /**
     * Recebe a requisição com a autenticação e realiza as validações necessárias
     *
     * @author joao.silva
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        this.startServices(httpServletRequest);

        //Busca pelo header "Authorization" por padrão
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Seleciona o token que por padrão inicia ao final da substring "Bearer "
            String token = authorizationHeader.substring(7);

            // Caso o token tenha sido decodificado com sucesso
            if (token != null && userService != null) {

                // Busca o usuário pelo token de acesso
                User userDB = userService.findOneUserByAccessToken(token);

                if (userDB != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    if (!userDB.tokenIsValid()) {
                        // Busca o novo token
                        String newToken = authService.refreshGoogleAuth(userDB);

                        // Busca ele no banco
                        httpServletResponse.addHeader( "Refresh","Bearer " + newToken);
                    }

                    // Cria o userDetails do usuário logado
                    UserDetails userDetails = dinoUserDetailsService.loadUserByUsername(userDB.getExternalId(), userDB.getAccessToken());

                    // Constroi o UsernamePasswordAuthenticationToken que serve como credenciais do usuário
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                    // Salva as credenciais do usuário
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void startServices(HttpServletRequest httpServletRequest) {
        ServletContext servletContext = null;
        WebApplicationContext webApplicationContext = null;

        if(userService == null){
            servletContext = httpServletRequest.getServletContext();
            webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            userService = webApplicationContext.getBean(UserServiceImpl.class);
        }

        if(authService == null){
            if (servletContext == null) {
                servletContext = httpServletRequest.getServletContext();
            }
            if (webApplicationContext == null) {
                webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            }
            authService = webApplicationContext.getBean(AuthServiceImpl.class);
        }

        if(dinoUserDetailsService == null){
            if (servletContext == null) {
                servletContext = httpServletRequest.getServletContext();
            }
            if (webApplicationContext == null) {
                webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            }
            dinoUserDetailsService = webApplicationContext.getBean(DinoUserDetailsService.class);
        }
    }
}
