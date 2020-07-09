package br.ufrgs.inf.pet.dinoapi.controller.websocket.glossary;

import br.ufrgs.inf.pet.dinoapi.model.websocket.Greeting;
import br.ufrgs.inf.pet.dinoapi.model.websocket.SubscribeResponse;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
public class GlossaryWebSocketControllerImpl implements GlossaryWebSocketController {

    @Override
    @SendTo("/topic/glossary")
    public SubscribeResponse subscribe(Principal user) {
        greetingService.addUserName(principal.getName());
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
