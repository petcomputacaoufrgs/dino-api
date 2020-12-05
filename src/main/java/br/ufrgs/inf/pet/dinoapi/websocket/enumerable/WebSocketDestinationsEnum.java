package br.ufrgs.inf.pet.dinoapi.websocket.enumerable;

public enum WebSocketDestinationsEnum {
    GLOSSARY_UPDATE(generateTopicDest("glossary/update/")),
    GLOSSARY_DELETE(generateTopicDest("glossary/delete/")),
    ALERT_APP_SETTINGS_UPDATE("/queue/user_app_settings/update"),
    ALERT_NOTE_UPDATE("/queue/note/update"),
    ALERT_NOTE_ORDER_UPDATE("/queue/note/order/update"),
    ALERT_NOTE_DELETE("/queue/note/delete"),
    ALERT_USER_UPDATE("/queue/user/update"),
    ALERT_CONTACT_UPDATE("/queue/contact/update"),
    ALERT_FAQ_UPDATE("/topic/faq/update"),
    ALERT_FAQ_USER_UPDATE("/queue/faq/update/user"),
    ALERT_NOTE_COLUMN_UPDATE("/queue/note_column/update"),
    ALERT_NOTE_COLUMN_ORDER_UPDATE("/queue/note_column/order/update"),
    ALERT_NOTE_COLUMN_DELETE("/queue/note_column/delete"),
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