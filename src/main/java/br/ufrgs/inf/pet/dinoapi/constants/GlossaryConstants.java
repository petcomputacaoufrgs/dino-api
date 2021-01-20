package br.ufrgs.inf.pet.dinoapi.constants;

public final class GlossaryConstants {
    public final static String ITEM_ID_NULL_MESSAGE = "id from item should not be null..";

    public final static String ITEM_LIST_NULL_MESSAGE = "itemList should not be null..";

    public final static String VERSION_NULL_MESSAGE = "version should not be null..";

    public final static String EXISTS_NULL_MESSAGE = "exists should not be null..";

    public final static int TITLE_MAX = 50;
    public final static int TITLE_MIN = 1;
    public final static String TITLE_MESSAGE = "title should be between " + TITLE_MIN + " and " + TITLE_MAX + " characters.";
    public final static String TITLE_NULL_MESSAGE = "title should not be null..";

    public final static int TEXT_MAX = 1000;
    public final static String TEXT_MESSAGE = "text can not have more than " + TEXT_MAX + " characters.";

    public final static int SUBTITLE_MAX = 20;
    public final static String SUBTITLE_MESSAGE = "subtitle can not have more than " + SUBTITLE_MAX + " characters.";

    public final static int FULLTEXT_MAX = 10000;
    public final static String FULLTEXT_MESSAGE = "fullText can not have more than " + FULLTEXT_MAX + " characters.";
}
