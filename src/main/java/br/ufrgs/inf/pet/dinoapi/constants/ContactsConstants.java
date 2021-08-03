package br.ufrgs.inf.pet.dinoapi.constants;

public final class ContactsConstants {
    public final static int NAME_MAX = 100;
    public final static String NAME_MAX_MESSAGE = "name should be between 1 and " + NAME_MAX;
    public final static String NAME_NULL_MESSAGE = "name cannot be null";

    public final static int DESCRIPTION_MAX = 500;
    public final static String DESCRIPTION_MAX_MESSAGE = "description should not be more than " + DESCRIPTION_MAX;

    public final static int NUMBER_MAX = 30;
    public final static String NUMBER_NULL_MESSAGE = "phone number cannot be null";
    public final static String NUMBER_MAX_MESSAGE = "number should be between 1 and " + NUMBER_MAX;

    public final static String TYPE_NULL_MESSAGE = "phone type cannot be null.";

    public final static String PHONE_WITHOUT_CONTACT = "phone need to have a contact";

    public final static String PHONE_INVALID_CONTACT = "invalid contact for phone";

    public final static String ESSENTIAL_CONTACT_ID_NULL_MESSAGE = "essential contact id for phone cannot be null";

    public final static String ESSENTIAL_CONTACT_NOT_FOUND = "essential contact not found";

}
