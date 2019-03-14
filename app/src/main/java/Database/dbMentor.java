package Database;

public final class dbMentor {

    private dbMentor(){

    }

    public static final String TABLE_NAME = "mentor";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MENTORNAME = "title";
    public static final String COLUMN_MENTOREMAIL = "email";
    public static final String COLUMN_PHONE = "phone";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_MENTORNAME + " TEXT, " + COLUMN_MENTOREMAIL + " TEXT,"  + COLUMN_PHONE  + " TEXT)";

}
