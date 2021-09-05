package br.ufrgs.inf.pet.dinoapi.constants;

public final class PathConstants {
    public final static String GET = "get/";
    public final static String SAVE = "save/";
    public final static String DELETE = "delete/";
    public final static String GET_ALL = GET + "all/";
    public final static String SAVE_ALL = SAVE + "all/";
    public final static String DELETE_ALL = DELETE + "all/";
    public final static String SYNC = "sync/";

    // td autenticado AUTH
    public final static String ROUTE_PRIVATE = "/private/";
    // staff n pode CLIENT
    public final static String ROUTE_USER = "/user/";
    public final static String ROUTE_STAFF = "/staff/";

    public final static String EVENT =              ROUTE_PRIVATE + "event/";
    public final static String EVENT_TYPE =         ROUTE_PRIVATE + "event_type/";
    public final static String GLOSSARY =           ROUTE_PRIVATE + "glossary/";
    public final static String FAQ_ITEM =           ROUTE_PRIVATE + "faq_item/";
    public final static String GOOGLE_SCOPE =       ROUTE_PRIVATE + "auth/google/scope/";
    public final static String ESSENTIAL_CONTACT =  ROUTE_PRIVATE + "essential_contact/";
    public final static String ESSENTIAL_PHONE =    ROUTE_PRIVATE + "essential_phone/";
    public final static String TREATMENT =          ROUTE_PRIVATE + "treatment/";
    public final static String TREATMENT_QUESTION = ROUTE_PRIVATE + "treatment_question/";
    public final static String REPORT =             ROUTE_PRIVATE + "report/";
    public final static String USER =               ROUTE_PRIVATE + "user/";
    public final static String SETTINGS =           ROUTE_PRIVATE + "settings/";
    public final static String LOG_APP_ERROR =      ROUTE_PRIVATE + "log_app_error/";
    public final static String LOG_APP_ERROR_ALL =  LOG_APP_ERROR + "all/";
    public final static String USER_DELETE_ACCOUNT= USER + "delete_account/";

    public final static String CONTACT =        ROUTE_USER + "contact/";
    public final static String PHONE =          ROUTE_USER + "phone/";
    public final static String NOTE_COLUMN =    ROUTE_USER + "note_column/";
    public final static String NOTE =           ROUTE_USER + "note/";

    public final static String STAFF = ROUTE_STAFF + "staff_management/";

    public final static String KIDS_SPACE_SETTINGS = "/kids_space_settings/";
}
