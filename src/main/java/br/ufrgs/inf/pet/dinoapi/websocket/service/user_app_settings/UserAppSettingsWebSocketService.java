package br.ufrgs.inf.pet.dinoapi.websocket.service.user_app_settings;

public interface UserAppSettingsWebSocketService {
    void sendUpdateMessage(Long newVersion);
}