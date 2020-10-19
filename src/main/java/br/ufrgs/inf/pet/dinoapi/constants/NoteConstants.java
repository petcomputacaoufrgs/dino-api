package br.ufrgs.inf.pet.dinoapi.constants;

public final class NoteConstants {

        public final static String ID_NULL_MESSAGE = "id cannot be null.";

        public final static String QUESTION_NULL_MESSAGE = "question cannot be null.";
        public final static int QUESTION_MAX = 250;
        public final static int QUESTION_MIN = 1;
        public final static String QUESTION_SIZE_MESSAGE = "question should be between 1 and 250.";

        public final static int MAX_TAGS = 5;
        public final static String MAX_TAGS_MESSAGE = "a note can not have more than 5 tags.";

        public final static String LAST_UPDATE_NULL_MESSAGE = "lastUpdate cannot be null.";

        public final static String ANSWER_NULL_MESSAGE = "answer cannot be null.";
        public final static int ANSWER_MAX = 500;
        public final static String ANSWER_SIZE_MESSAGE = "answer should be between 0 and 500.";

        public final static String ORDER_NULL_MESSAGE = "order cannot be null.";

        public final static String LAST_ORDER_UPDATE_NULL_MESSAGE = "last order update cannot be null.";

        public final static String COLUMN_TITLE_NULL_MESSAGE = "columnTitle cannot be null.";

        public final static String COLUMN_ID_NULL_MESSAGE = "id cannot be null.";

        public final static int MAX_NOTES_PER_COLUMN = 30;
        public final static String MAX_NOTES_PER_COLUMN_MESSAGE = "a column can't have more than 30 notes.";

        public final static String NOTE_NOT_FOUND_MESSAGE = "note not found";

        public final static String NOTE_NOT_FOUND_IN_ORDER_MESSAGE_PT1 = "Total of ";

        public final static String NOTE_NOT_FOUND_IN_ORDER_MESSAGE_PT2 = " items found of ";

        public final static String NOTE_NOT_FOUND_IN_ORDER_MESSAGE_PT3 = " items.";

        public final static String LAST_UPDATE_ORDER_NULL_MESSAGE = "lastUpdateOrder cannot be null.";

        public final static String CHANGED_NOTES_NULL_MESSAGE = "changedNotes cannot be null.";

        public final static String NEW_NOTES_NULL_MESSAGE = "newNotes cannot be null.";

        public final static String DELETED_NOTES_NULL_MESSAGE = "deletedNotes cannot be null.";

        public final static String NOTE_ORDER_NULL_MESSAGE = "notesOrder cannot be null.";

}
