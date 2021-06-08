package br.ufrgs.inf.pet.dinoapi.constants;

public final class PathConstants {
    public final static String GET = "get/";
    public final static String SAVE = "save/";
    public final static String DELETE = "delete/";
    public final static String GET_ALL = GET + "all/";
    public final static String SAVE_ALL = SAVE + "all/";
    public final static String DELETE_ALL = DELETE + "all/";
    public final static String SYNC = "sync/";

    public final static String PRIVATE = "/private/";
    public final static String USER = "/user/";
    public final static String ADMIN = "/admin/";

    public final static String GLOSSARY = PRIVATE + "glossary/";
    public final static String FAQ_ITEM = PRIVATE + "faq_item/";
    public final static String GOOGLE_SCOPE = PRIVATE + "auth/google/scope/";
    public final static String ESSENTIAL_CONTACT = PRIVATE + "essential_contact/";
    public final static String ESSENTIAL_PHONE = PRIVATE + "essential_phone/";
    public final static String TREATMENT = PRIVATE + "treatment/";
    public final static String TREATMENT_QUESTION = PRIVATE + "treatment_question/";
    public final static String LOG_APP_ERROR = PRIVATE + "log_app_error/";
    public final static String LOG_APP_ERROR_ALL = PRIVATE + LOG_APP_ERROR + "all/";

    public final static String CONTACT = USER + "contact/";
    public final static String PHONE = USER + "phone/";
    public final static String NOTE_COLUMN = USER + "note_column/";
    public final static String NOTE = USER + "note/";

    public final static String STAFF = ADMIN + "staff/";

    public final static String KIDS_SPACE_SETTINGS = "/kids_space_settings/";
}
