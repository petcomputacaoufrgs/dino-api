package br.ufrgs.inf.pet.dinoapi.websocket.enumerable;

public enum WebSocketDestinationsEnum {
    GOOGLE_SCOPE("google_scope"),
    PHONE("phone"),
    GOOGLE_CONTACT("google_contact"),
    CONTACT("contact"),
    ESSENTIAL_CONTACT("essential_contact"),
    GLOSSARY("glossary"),
    NOTE("note"),
    NOTE_COLUMN("note_column"),
    USER("user"),
    USER_SETTINGS("user_settings"),
    TREATMENT("treatment"),
    FAQ_ITEM("faq_item"),
    FAQ("faq"),
    FAQ_USER_QUESTION("faq_user_question"),
    LOGOUT_REQUEST("logout_request");

    private String value;

    WebSocketDestinationsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}