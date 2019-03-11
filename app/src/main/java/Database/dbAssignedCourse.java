package Database;

public final class dbAssignedCourse {

    private dbAssignedCourse(){

    }

    public static final String TABLE_NAME = "assignedcourse";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TERMNAME = "title";
    public static final String COLUMN_COURSENAME = "course";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TERMNAME + " TEXT, " + COLUMN_COURSENAME + " TEXT)";

}
