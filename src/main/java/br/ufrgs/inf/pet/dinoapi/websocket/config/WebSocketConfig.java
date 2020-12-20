package br.ufrgs.inf.pet.dinoapi.websocket.config;

import br.ufrgs.inf.pet.dinoapi.configuration.application_properties.AppConfig;
import br.ufrgs.inf.pet.dinoapi.websocket.interceptor.WebSocketUserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final AppConfig appConfig;

    @Autowired
    public WebSocketConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        final String origin = appConfig.getOrigin();
        registry
                .addEndpoint("/websocket")
                .setHandshakeHandler(new WebSocketUserInterceptor())
                .setAllowedOrigins(origin)
                .withSockJS();
    }
}
