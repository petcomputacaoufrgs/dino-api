package br.ufrgs.inf.pet.dinoapi.controller.websocket;

import br.ufrgs.inf.pet.dinoapi.model.websocket.Greeting;
import br.ufrgs.inf.pet.dinoapi.model.websocket.HelloMessage;
import br.ufrgs.inf.pet.dinoapi.service.websocket.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import java.security.Principal;

@Controller
public class GreetingController {

    @Autowired
    private GreetingService greetingService;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message, Principal principal) throws Exception {
        greetingService.addUserName(principal.getName());
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageMapping("/greetings")
    @SendToUser("/queue/greetings")
    public String reply(@Payload String message, Principal user) {
        return  "Hello " + message;
    }
}
