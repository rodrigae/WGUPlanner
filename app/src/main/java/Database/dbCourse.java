package Database;

public final class dbCourse {

    private dbCourse(){

    }

    public static final String TABLE_NAME = "course";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TERMNAME = "title";
    public static final String COLUMN_STARTDATE = "start_date";
    public static final String COLUMN_ENDDATE = "end_date";
    public static final String COLUMN_STARTDATEREMINDER = "course_starts";
    public static final String COLUMN_ENDDATEREMINDER = "course_ends";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_ASSESSMENTID = "assesssment_id";
    public static final String COLUMN_MENTORID = "mentor_id";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TERMNAME + " TEXT, " + COLUMN_STARTDATE + " TEXT, " + COLUMN_ENDDATE + " TEXT, " +
            COLUMN_STARTDATEREMINDER + " TEXT, " +COLUMN_ENDDATEREMINDER + " TEXT, " +
            COLUMN_NOTES + " TEXT, " + COLUMN_ASSESSMENTID + " TEXT, " +
            COLUMN_MENTORID + " TEXT)";

}
