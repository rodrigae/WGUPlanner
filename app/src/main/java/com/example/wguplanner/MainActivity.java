package com.example.wguplanner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.TreeMap;

import Database.dbAssesssment;
import Database.dbAssignedCourse;
import Database.dbCourse;
import Database.dbSqlLiteManager;
import Database.dbTerm;
import Utilities.CourseData;
import Utilities.TermData;
import models.Course;
import models.Term;
import wguplanner_details.TermDetailsActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  protected  DrawerLayout drawer = null;
  protected FloatingActionButton fab = null;
  protected Toolbar toolbar = null;
  protected SQLiteDatabase database = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //used for termActivity, TermDetails
        LoadTermList();
        LoadCreatedCourses();//Load Course lists, used in TermDetailsActivity, and also CourseActivity
        AddAssignedCourseListToTermData();//load list of assigned course for later use
        LoadAvailableAssessmentList();//Load List of assessment
        LoadAssignedCourseList();// Load assign courses per term

        if (id == R.id.nav_term) {
            Intent course = new Intent(this, TermActivity.class);
            startActivity(course);
            item.setChecked(true);
        } else if (id == R.id.nav_course) {
            Intent course = new Intent(this, CourseActivity.class);
            startActivity(course);
            item.setChecked(true);
        } else if (id == R.id.nav_assessment) {
            Intent course = new Intent(this, AssessmentActivity.class);
            startActivity(course);
        } else if (id == R.id.nav_mentors) {
            Intent course = new Intent(this, MentorActivity.class);
            startActivity(course);
            item.setChecked(true);
        } else if (id == R.id.nav_home) {
            Intent course = new Intent(this, MainActivity.class);
            startActivity(course);
            item.setChecked(true);

        }else{
            item.setChecked(false);
        }



        return true;
    }

    protected void LoadTermList(){
        //load the term list
        Cursor c = null;
        TreeMap<String, Term> data = new TreeMap<>();
        try {
            Term termItem = null;
            //get the list
            //set the adapter for list
            //get the database access
            database = new dbSqlLiteManager(this).getReadableDatabase(); //get the database access
            c = database.rawQuery("SELECT title,start_date,end_date,notes FROM term ", null);
            if (c.moveToFirst()) {
                do {
                    termItem = new Term(c.getString(0), c.getString(1), c.getString(2), c.getString(3));
                    //Title, and all data
                    data.put(c.getString(0),termItem);//add the list of items to the treemap for later retrival

                } while (c.moveToNext());
            }
            TermData.setCreatedTermList(data);
            database.close();
        }catch(Exception e){
            if (e.getMessage().contains("no such table")){
                database = new dbSqlLiteManager(this).getWritableDatabase();
                database.execSQL(dbTerm.CREATE_TABLE);
            }
            e.printStackTrace();
        }
    }

    private void AddAssignedCourseListToTermData(){
        //load the Course list assigned and added to TermData for use
        Cursor c = null;
        try {
            // get the database access
            ArrayList<String> assignedCourseItem = new ArrayList<>();
           ArrayList<String> termsList = new ArrayList<>();
           String TermName = null;
            for (String Name : TermData.getCreatedTermList().keySet()){
                termsList.add(Name);
            }
           database = new dbSqlLiteManager(this).getReadableDatabase(); //get the database access
            for (int i=0; i<termsList.size(); i++) {
                TermName = termsList.get(i);
                c = database.rawQuery("SELECT "+ dbAssignedCourse.COLUMN_COURSENAME + " FROM " +dbAssignedCourse.TABLE_NAME+" WHERE " + dbAssignedCourse.COLUMN_TERMNAME+" = '" + TermName + "'", null);
                if (c.moveToFirst()) {
                    do {
                        assignedCourseItem.add(c.getString(0));// Add items to the adapter
                    } while (c.moveToNext());
                    TermData.addAssignCourseList(TermName, assignedCourseItem);
                }
            }
            database.close();
        }catch(Exception e){
            if (e.getMessage().contains("no such table")){
                database = new dbSqlLiteManager(this).getWritableDatabase();
                database.execSQL(dbAssignedCourse.CREATE_TABLE);
            }
            e.printStackTrace();
        }
    }

    private void LoadAvailableAssessmentList(){
        //load the Course list
        Cursor c = null;
        try {
            //get the list
            ListView AssessmentListAdpt = findViewById(R.id.CourseAvailableAssessmentList);
            //set the adapter for list
            ArrayList<String>  AvailableAssessmentItems = new ArrayList<>();
            // get the database access
            database = new dbSqlLiteManager(this).getReadableDatabase(); //get the database access
            c = database.rawQuery("SELECT "+ dbAssesssment.COLUMN_ASSESSMENTNAME+" FROM "+ dbAssesssment.TABLE_NAME, null);
            if (c.moveToFirst()) {
                do {
                    if (!CourseData.getAssignedAssessmentItem().contains(c.getString(0))) {
                        AvailableAssessmentItems.add(c.getString(0));// Add items to the adapter
                    }
                } while (c.moveToNext());
            }
            CourseData.setAssignedAssessmentItem(AvailableAssessmentItems);
            database.close();
        }catch(Exception e){
            if (e.getMessage().contains("no such table")){
                database = new dbSqlLiteManager(this).getWritableDatabase();
                database.execSQL(dbAssesssment.CREATE_TABLE);
            }
            e.printStackTrace();
        }
    }

    private void LoadAssignedCourseList(){
        //load the Course list assigned
        Cursor c = null;
        try {
            //get the list
            TreeMap<String, ArrayList<String>> AssignedCourses = new TreeMap<>();

            ArrayList<String> assignedCourseItem = new ArrayList<>();
            database = new dbSqlLiteManager(this).getReadableDatabase(); //get the database access

            for (String TermName : TermData.getCreatedTermList().keySet()) {
                c = database.rawQuery("SELECT course FROM assignedcourse WHERE title = '" + TermName + "'", null);
                if (c.moveToFirst()) {
                    do {
                        assignedCourseItem.add(c.getString(0));// Add items to the adapter
                    } while (c.moveToNext());

                }
                AssignedCourses.put(TermName, assignedCourseItem);

            }
            TermData.setAssignedCourses(AssignedCourses);
            database.close();
        }catch(Exception e){
            if (e.getMessage().contains("no such table")){
                database = new dbSqlLiteManager(this).getWritableDatabase();
                database.execSQL(dbAssignedCourse.CREATE_TABLE);
            }
            e.printStackTrace();
        }
    }

    protected void LoadCreatedCourses(){
        //load the term list
        Cursor c = null;
        TreeMap<String, Course> data = new TreeMap<>();
        try {
            Course courseItem = null;
            //get the list
            //get the database access
            database = new dbSqlLiteManager(this).getReadableDatabase(); //get the database access
            c = database.rawQuery("SELECT " + dbCourse.COLUMN_STARTDATEREMINDER+","+ dbCourse.COLUMN_ENDDATEREMINDER+","+ dbCourse.COLUMN_COURSENAME+","+dbCourse.COLUMN_STARTDATE+","+dbCourse.COLUMN_ENDDATE+","+dbCourse.COLUMN_NOTES+","+dbCourse.COLUMN_ASSESSMENTID+","+dbCourse.COLUMN_MENTORID+" FROM "+ dbCourse.TABLE_NAME, null);
            if (c.moveToFirst()) {
                do {
                    courseItem = new Course(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7));
                    data.put(c.getString(2),courseItem);//add the list of items to the treemap for later retrival
                } while (c.moveToNext());
            }
            CourseData.setCreatedCourse(data);
            database.close();
        }catch(Exception e){
            if (e.getMessage().contains("no such table")){
                database = new dbSqlLiteManager(this).getWritableDatabase();
                database.execSQL(dbTerm.CREATE_TABLE);
            }
            e.printStackTrace();
        }
    }

}
