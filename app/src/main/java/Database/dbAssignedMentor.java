package Database;

public final class dbAssignedMentor {

    private dbAssignedMentor(){

    }

    public static final String TABLE_NAME = "assignedmentor";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_COURSENAME = "title";
    public static final String COLUMN_MENTORNAME = "mentor";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_COURSENAME + " TEXT, " + COLUMN_MENTORNAME + " TEXT)";

}
