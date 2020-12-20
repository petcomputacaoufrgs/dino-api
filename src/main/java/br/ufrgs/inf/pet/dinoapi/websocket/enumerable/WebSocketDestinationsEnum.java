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
    USER_SETTINGS_UPDATE(generateQueueDest("user_settings/update/")),
    USER_SETTINGS_DELETE(generateQueueDest("user_settings/delete/")),
    TREATMENT_UPDATE(generateTopicDest("treatment/update/")),
    TREATMENT_DELETE(generateTopicDest("treatment/delete/")),
    FAQ_ITEM_UPDATE(generateTopicDest("faq_item/update/")),
    FAQ_ITEM_DELETE(generateTopicDest("faq_item/delete/")),
    FAQ_UPDATE(generateTopicDest("faq/update/")),
    FAQ_DELETE(generateTopicDest("faq/delete/")),
    FAQ_USER_QUESTION_UPDATE(generateTopicDest("faq_user_question/update/")),
    FAQ_USER_QUESTION_DELETE(generateTopicDest("faq_user_question/delete/")),
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