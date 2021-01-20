package br.ufrgs.inf.pet.dinoapi.constants;

public final class NoteConstants {
    public final static String QUESTION_NULL_MESSAGE = "question cannot be null.";
    public final static int QUESTION_MAX = 250;
    public final static int QUESTION_MIN = 1;
    public final static String QUESTION_SIZE_MESSAGE = "question should be between 1 and 250.";

    public final static int MAX_TAGS_SIZE = 210;
    public final static String MAX_TAGS_MESSAGE = "tags can not have more than 210 characters.";

    public final static int ANSWER_MAX = 500;
    public final static String ANSWER_SIZE_MESSAGE = "answer should be between 0 and 500.";

    public final static String ORDER_NULL_MESSAGE = "order cannot be null.";

    public final static String COLUMN_ID_NULL_MESSAGE = "column id cannot be null";

    public final static String COLUMN_TITLE_NULL_MESSAGE = "columnTitle cannot be null.";
    public final static int COLUMN_TITLE_MAX = 250;
    public final static int COLUMN_TITLE_MIN = 1;
    public final static String COLUMN_TITLE_SIZE_MESSAGE = "title should be between " + COLUMN_TITLE_MIN + " and " + COLUMN_TITLE_MAX + ".";

    public final static String INVALID_COLUMN = "Column not found";
}
