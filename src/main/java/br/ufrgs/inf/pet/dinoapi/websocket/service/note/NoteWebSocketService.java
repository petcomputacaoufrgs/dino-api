package br.ufrgs.inf.pet.dinoapi.websocket.service.note;

public interface NoteWebSocketService {
    void sendNoteUpdateMessage(Long newVersion);
}
