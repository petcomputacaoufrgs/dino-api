package br.ufrgs.inf.pet.dinoapi.constants;

public final class LogAppErrorConstants {
    public final static int TITLE_MAX = 50000;
    public final static String TITLE_MESSAGE = "title should not be more than 10000.";

    public final static int FILE_MAX = 1000;
    public final static String FILE_MESSAGE = "file should not be more than 500.";

    public final static int MESSAGE_MAX = 5000;

    public final static int STACK_TRACE_MAX = 100000;
    public final static String STACK_TRACE_MESSAGE = "error should not be more than 20000.";
    public final static String STACK_TRACE_NULL_MESSAGE = "error should not be null.";

    public final static String DATE_NULL_MESSAGE = "date should not be null.";

    public final static String ITEMS_NULL_MESSAGE = "items should not be null.";
}
