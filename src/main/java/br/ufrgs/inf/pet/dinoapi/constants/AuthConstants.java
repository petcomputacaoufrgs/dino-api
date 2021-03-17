package br.ufrgs.inf.pet.dinoapi.constants;

public final class AuthConstants {
    public final static int ACCESS_TOKEN_MAX = 560;

    public final static int REFRESH_TOKEN_MAX = 560;

    public final static int WS_TOKEN_MAX = 560;

    public final static int NAME_MAX = 100;

    public final static int RESPONSIBLE_TOKEN_MAX = 560;

    public final static int RESPONSIBLE_CODE_LENGTH = 20;

    public final static String INVALID_AUTH = "Authentication error";

    public final static String UNKNOWN_ERROR = "Unknown server error";

    public final static String ERROR_READING_CONFIG_FILES = "Internal server error";

    public final static String RECOVER_REQUEST_FOUND_NOT_FOUND_ERROR = "Request code not found";

    public final static String RECOVER_REQUEST_MAX_ATTEMPTS_ERROR = "Max attempts";

    public final static  Short MIN_DELAY_TO_REQUEST_CODE_MIN = 2;

    public final static Short MAX_DELAY_TO_RECOVER_PASSWORD_MIN = 60;

    public final static Short MAX_ATTEMPTS = 3;
}
