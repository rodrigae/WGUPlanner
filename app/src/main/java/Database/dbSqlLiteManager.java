package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbSqlLiteManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WGUPlanner";

    public dbSqlLiteManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(dbTerm.CREATE_TABLE);
        db.execSQL(dbCourse.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + dbTerm.TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + dbCourse.TABLE_NAME);
    onCreate(db);
    }
}
