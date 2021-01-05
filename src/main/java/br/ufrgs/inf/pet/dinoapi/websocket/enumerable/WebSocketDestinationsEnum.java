package br.ufrgs.inf.pet.dinoapi.websocket.enumerable;

public enum WebSocketDestinationsEnum {
    GOOGLE_SCOPE("auth/google/scope"),
    PHONE("phone"),
    GOOGLE_CONTACT("google_contact"),
    CONTACT("contact"),
    GLOSSARY("glossary"),
    NOTE("note"),
    NOTE_COLUMN("note_column"),
    USER("user"),
    USER_SETTINGS("user_settings"),
    TREATMENT("treatment"),
    FAQ_ITEM("faq_item"),
    FAQ("faq"),
    FAQ_USER_QUESTION("faq_user_question");

    private String value;

    WebSocketDestinationsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}