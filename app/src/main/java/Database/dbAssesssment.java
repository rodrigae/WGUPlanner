package Database;

public final class dbAssesssment {

    private dbAssesssment(){

    }

    public static final String TABLE_NAME = "assessment";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ASSESSMENTNAME = "title";
    public static final String COLUMN_DUEDATE = "due_date";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_REMINDER = "reminder";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_ASSIGNCOURSE = "course";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ASSESSMENTNAME + " TEXT, " + COLUMN_DUEDATE + " TEXT, " + COLUMN_STATUS + " TEXT, " + COLUMN_ASSIGNCOURSE + " TEXT, " + COLUMN_TYPE + " TEXT, " +
            COLUMN_REMINDER + " TEXT)";

}
