package br.ufrgs.inf.pet.dinoapi.constants;

public final class FaqConstants {
    public final static String ID_NULL_MESSAGE = "id cannot be null.";

    public final static int TITLE_MIN = 1;
    public final static int TITLE_MAX = 30;
    public final static String TITLE_MAX_MESSAGE = "title should be between " + TITLE_MIN + " and " + TITLE_MAX;
    public final static String TITLE_NULL_MESSAGE = "title should not be null..";

    public final static String TREATMENT_ID_NULL_MESSAGE = "treatmentId cannot be null";

    public final static int QUESTION_MIN = 1;
    public final static int QUESTION_MAX = 100;
    public final static String QUESTION_SIZE_MESSAGE = "question should be between " + QUESTION_MIN + " and " + QUESTION_MAX;
    public final static String QUESTION_NULL_MESSAGE = "question should not be null.";

    public final static int USER_QUESTION_MAX = 200;

    public final static int ANSWER_MIN = 1;
    public final static int ANSWER_MAX = 1000;
    public final static String ANSWER_MESSAGE = "answer should be between " + ANSWER_MIN + " and " + ANSWER_MAX;
    public final static String ANSWER_NULL_MESSAGE = "answer should not be null.";

    public final static String FAQ_ID_NULL_MESSAGE = "faqId should not be null.";

    public final static String FAQ_USER_QUESTION_SUCCESS = "success";

    public final static String INVALID_FAQ = "invalid FAQ";


}
