package br.ufrgs.inf.pet.dinoapi.websocket.service.glossary;

public interface GlossaryWebSocketService {
    void sendGlossaryUpdateMessage(Long newVersion);
}
