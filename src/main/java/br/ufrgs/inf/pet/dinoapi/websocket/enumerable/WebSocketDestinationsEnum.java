package br.ufrgs.inf.pet.dinoapi.websocket.enumerable;

public enum WebSocketDestinationsEnum {
    ALERT_GLOSSARY_UPDATE("/topic/glossary/update"),
    ALERT_APP_SETTINGS_UPDATE("/queue/user_app_settings/update"),
    ALERT_NOTE_UPDATE("/queue/note/update"),
    ALERT_USER_UPDATE("/queue/user/update"),
    ALERT_CONTACT_UPDATE("/queue/contact/update");

    private String value;

    WebSocketDestinationsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
