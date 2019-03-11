package Database;

public final class dbTerm {

    private dbTerm(){

    }

    public static final String TABLE_NAME = "term";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TERMNAME = "title";
    public static final String COLUMN_STARTDATE = "start_date";
    public static final String COLUMN_ENDDATE = "end_date";
    public static final String COLUMN_NOTES = "notes";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TERMNAME + " TEXT, " + COLUMN_STARTDATE + " TEXT, " + COLUMN_ENDDATE + " TEXT, " +
            COLUMN_NOTES + " TEXT)";

}
