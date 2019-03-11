package Database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import models.Course;
import models.Term;
public class dbStatements {
    public static boolean SaveTermDetails(Object obj, SQLiteDatabase database) {
        //save term details data to the database
        try {
            if (obj instanceof Term) {


                ContentValues values = new ContentValues();
                values.put(dbTerm.COLUMN_TERMNAME, ((Term) obj).getTitle());
                values.put(dbTerm.COLUMN_STARTDATE, ((Term) obj).getStartDate());
                values.put(dbTerm.COLUMN_ENDDATE, ((Term) obj).getEndDate());
                values.put(dbTerm.COLUMN_NOTES, ((Term) obj).getNotes());
                long newRowId = database.insert(dbTerm.TABLE_NAME, null, values);

                return true;
            }
            } catch(Exception e){
                e.printStackTrace();
            }

        return false;

    }


    public static boolean SaveCourseDetails(Object obj, SQLiteDatabase database) {
        //save course details data to the database
        try {
            if (obj instanceof Course) {
                ContentValues values = new ContentValues();
                values.put(dbCourse.COLUMN_COURSENAME, ((Course) obj).getTitle());
                values.put(dbCourse.COLUMN_STARTDATE, ((Course) obj).getStartDate());
                values.put(dbCourse.COLUMN_ENDDATE, ((Course) obj).getEndDate());
                values.put(dbCourse.COLUMN_NOTES, ((Course) obj).getNotes());
                values.put(dbCourse.COLUMN_STARTDATEREMINDER, ((Course) obj).getReminderStartDate());
                values.put(dbCourse.COLUMN_ENDDATEREMINDER, ((Course) obj).getIsReminderEndDate());
                long newRowId = database.insert(dbCourse.TABLE_NAME, null, values);
                return true;
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }

    public static boolean SaveAssignedTermCourse(Object obj, SQLiteDatabase database, List<String> courseList) {
        //save the courses assigned for the term
        try {
            if (obj instanceof Term) {
                ContentValues values = null;

               for (int i = 0; i < courseList.size(); i++){
                        values = new ContentValues();
                        values.put(dbAssignedCourse.COLUMN_TERMNAME, ((Term) obj).getTitle());
                        values.put(dbAssignedCourse.COLUMN_COURSENAME, courseList.get(i));
                        long newRowId = database.insert(dbAssignedCourse.TABLE_NAME, null, values);
               }

                return true;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;

    }

    public static void deleteAssignedCourses(String TermName, SQLiteDatabase database){
        //remove the term assigned courses
        try{
            database.execSQL("DELETE FROM "+dbAssignedCourse.TABLE_NAME +" WHERE " + dbAssignedCourse.COLUMN_TERMNAME +" = '"+TermName+"'");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteTerm(String TermName, SQLiteDatabase database){
        try{
            //remove the term from the database
            database.execSQL("DELETE FROM "+ dbTerm.TABLE_NAME+ " WHERE "+ dbTerm.COLUMN_TERMNAME + " = '"+TermName+"'");

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static boolean SaveAssignedCourseAssessment(Object obj, SQLiteDatabase database, List<String> AssessmentList) {
        //save the assessment assigned for the term
        try {
            if (obj instanceof Course) {
                ContentValues values = null;

                for (int i = 0; i < AssessmentList.size(); i++){
                    values = new ContentValues();
                    values.put(dbAssignedAssessment.COLUMN_ASSESSMENTNAME, ((Course) obj).getTitle());
                    values.put(dbAssignedAssessment.COLUMN_COURSENAME, AssessmentList.get(i));
                    long newRowId = database.insert(dbAssignedAssessment.TABLE_NAME, null, values);
                }
                return true;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;

    }

    public static void deleteAssignedAssessment(String CourseName, SQLiteDatabase database){
        //remove the term assigned courses
        try{
            database.execSQL("DELETE FROM "+dbAssignedAssessment.TABLE_NAME +" WHERE " + dbAssignedAssessment.COLUMN_COURSENAME +" = '"+CourseName+"'");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteCourse(String CourseName, SQLiteDatabase database){
        try{
            //remove the course from the database
            database.execSQL("DELETE FROM "+ dbCourse.TABLE_NAME+ " WHERE "+ dbCourse.COLUMN_COURSENAME + " = '"+CourseName+"'");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}


