package br.ufrgs.inf.pet.dinoapi.constants;

public class UserConstants {
    public final static int PICTURE_URL_MAX = 500;
    public final static String PICTURE_URL_MAX_MESSAGE = "pictureURL should be between 1 and " + PICTURE_URL_MAX + " characters";
    public final static String PICTURE_URL_NULL_MESSAGE = "pictureURL cannot be null";

    public final static int NAME_MAX = 100;
    public final static String NAME_MAX_MESSAGE = "name should be between 1 and "+NAME_MAX+" characters";
    public final static String NAME_MULL_MESSAGE = "name cannot be null.";

    public final static int EMAIL_MAX = 100;
}
