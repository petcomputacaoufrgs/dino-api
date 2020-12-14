package br.ufrgs.inf.pet.dinoapi.websocket.enumerable;

public enum WebSocketDestinationsEnum {
    PHONE_UPDATE(generateQueueDest("phone/update/")),
    PHONE_DELETE(generateQueueDest("phone/delete/")),
    GOOGLE_CONTACT_UPDATE(generateQueueDest("google_contact/update/")),
    GOOGLE_CONTACT_DELETE(generateQueueDest("google_contact/delete/")),
    CONTACT_UPDATE(generateQueueDest("contact/update/")),
    CONTACT_DELETE(generateQueueDest("contact/delete/")),
    GLOSSARY_UPDATE(generateTopicDest("glossary/update/")),
    GLOSSARY_DELETE(generateTopicDest("glossary/delete/")),
    NOTE_UPDATE(generateQueueDest("note/update/")),
    NOTE_DELETE(generateQueueDest("note/delete/")),
    NOTE_COLUMN_UPDATE(generateQueueDest("note_column/update/")),
    NOTE_COLUMN_DELETE(generateQueueDest("note_column/delete/")),
    USER_UPDATE(generateQueueDest("user/update/")),
    USER_DELETE(generateQueueDest("user/delete/")),
    ALERT_APP_SETTINGS_UPDATE("/queue/user_app_settings/update"),
    ALERT_FAQ_UPDATE("/topic/faq/update"),
    ALERT_FAQ_USER_UPDATE("/queue/faq/update/user"),
    ALERT_AUTH_SCOPE_UPDATE("/queue/auth/scope/update");

    private String value;

    WebSocketDestinationsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static String generateTopicDest(String dest) {
        return "/topic/" + dest;
    }

    public static String generateQueueDest(String dest) {
        return "/queue/" + dest;
    }
}