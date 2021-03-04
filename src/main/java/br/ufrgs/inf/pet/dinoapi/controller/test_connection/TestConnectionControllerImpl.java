package br.ufrgs.inf.pet.dinoapi.controller.test_connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestConnectionControllerImpl implements TestConnectionController {

    private JavaMailSender javaMailSender;

    @Autowired
    public TestConnectionControllerImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @GetMapping("public/test_connection/")
    public ResponseEntity<Void> get() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("jpedross1999@gmail.com");
        msg.setSubject("Tudo bem?");
        msg.setText("Olá, tudo bem?");

        javaMailSender.send(msg);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
