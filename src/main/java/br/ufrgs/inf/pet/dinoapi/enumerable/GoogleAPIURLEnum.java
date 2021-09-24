package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum GoogleAPIURLEnum {
    TOKEN_REQUEST("https://oauth2.googleapis.com/token"),
    CREATE_CONTACT("https://people.googleapis.com/v1/people:createContact"),
    UPDATE_CONTACT_BASE("https://people.googleapis.com/v1/"),
    GET_CONTACT_BASE("https://people.googleapis.com/v1/"),
    DELETE_CONTACT_BASE("https://people.googleapis.com/v1/"),

    CALENDARS("https://www.googleapis.com/calendar/v3/calendars");
    private String value;

    GoogleAPIURLEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
