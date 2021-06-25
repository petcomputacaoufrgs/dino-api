package br.ufrgs.inf.pet.dinoapi.constants;

public final class FaqConstants {
    public final static String ID_NULL_MESSAGE = "id cannot be null.";

    public final static String TREATMENT_ID_NULL_MESSAGE = "treatmentId cannot be null";

    public final static int QUESTION_MIN = 1;
    public final static int QUESTION_MAX = 100;
    public final static String QUESTION_SIZE_MESSAGE = "question should be between " + QUESTION_MIN + " and " + QUESTION_MAX;
    public final static String QUESTION_NULL_MESSAGE = "question should not be null.";

    public final static int ANSWER_MIN = 1;
    public final static int ANSWER_MAX = 1000;
    public final static String ANSWER_MESSAGE = "answer should be between " + ANSWER_MIN + " and " + ANSWER_MAX;
    public final static String ANSWER_NULL_MESSAGE = "answer should not be null.";

    public final static String INVALID_TREATMENT = "invalid treatment";

}
