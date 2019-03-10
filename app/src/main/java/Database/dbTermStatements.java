package Database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import models.Term;
public class dbTermStatements {
    public static boolean SaveTermDetails(Object obj, SQLiteDatabase database) {
        //save term details data to the database
        try {
            if (obj instanceof Term) {

                ContentValues values = new ContentValues();
                values.put(dbTerm.COLUMN_TERMNAME, ((Term) obj).getTitle());
                values.put(dbTerm.COLUMN_STARTDATE, ((Term) obj).getStartDate());
                values.put(dbTerm.COLUMN_ENDDATE, ((Term) obj).getEndDate());
                values.put(dbTerm.COLUMN_NOTES, ((Term) obj).getNotes());
                values.put(dbTerm.COLUMN_STARTDATEREMINDER, ((Term) obj).getReminderStartDate());
                values.put(dbTerm.COLUMN_ENDDATEREMINDER, ((Term) obj).getIsReminderEndDate());

                long newRowId = database.insert(dbTerm.TABLE_NAME, null, values);

                return true;
            }
            } catch(Exception e){
                e.printStackTrace();
            }

        return false;

    }
}


