package wguplanner_details;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.wguplanner.CourseActivity;
import com.example.wguplanner.MainActivity;
import com.example.wguplanner.R;

import Database.dbSqlLiteManager;
import Database.dbStatements;
import Utilities.CourseData;
import models.Course;


public class CourseDetailsActivity extends MainActivity {
    private Course Course = null;
    private EditText edittext = null;
    private CheckBox chk = null;
    private String msg = null;
    private  View contentView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.app_main_course_detail, null, false);
        // drawer.addView(contentView, 0);
        drawer.addView(contentView,0);

        //obtain access from the database
        database = new dbSqlLiteManager(this).getWritableDatabase();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so lon
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
                    Course = new Course(getData("startreminder"),getData("endreminder"),getData("CourseTitle") ,getData("startdate") , getData("enddate"), getData("notes"), getData("assessmentId"),getData("MentorId"));
                    if (validateEntry()) {
                        if (dbStatements.SaveCourseDetails(Course, database)) {
                            startActivity(new Intent(CourseDetailsActivity.this, CourseActivity.class));
                        } else {
                            Snackbar.make(contentView, "Failed Saving New Course", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }else{
                        Snackbar.make(contentView, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                   }

        }
            return true;
        }




    private boolean validateEntry() {
        //make sure these fields are not empty before proceeding
        if (Course.getTitle().isEmpty()){
            msg = "Title is empty";
            setFocusOnField("CourseTitle");
            return false;
        }else if (CourseData.getCourseList().containsKey(Course.getTitle())){
            msg = Course.getTitle() + " already exists. ";
            setFocusOnField("CourseTitle");
            return false;
        }else if (Course.getStartDate().isEmpty()){
            msg = "Start date is empty";
            setFocusOnField("startdate");
            return false;
        }else if (Course.getEndDate().isEmpty()){
            msg = "End date is empty";
            setFocusOnField("enddate");
            return false;
        }else if (Course.getStartDate().length() != 10){
            msg = "wrong format date, please use mm/dd/yyyy";
            setFocusOnField("startdate");
            return false;
        }else if (Course.getEndDate().length() != 10){
            msg = "wrong format date, please use mm/dd/yyyy";
            setFocusOnField("enddate");
            return false;
        }
        return true;

    }
    private void setFocusOnField(String field){
        switch (field) {
            case "CourseTitle":
                edittext = findViewById(R.id.CourseTitleEditText);
                edittext.requestFocus();
                break;
            case "startdate":
                edittext = findViewById(R.id.StartDateEditText);
                edittext.requestFocus();
                break;
            case "enddate":
                edittext = findViewById(R.id.EndDateEditText);
                edittext.requestFocus();
                break;

        }
    }
    private String getData(String item) {
        String results = null;

        switch (item) {
            case "CourseTitle":
                edittext = findViewById(R.id.CourseTitleEditText);
                results = edittext.getText().toString();
                break;
            case "startdate":
                edittext = findViewById(R.id.StartDateEditText);
                results = edittext.getText().toString();
                break;
            case "enddate":
                edittext = findViewById(R.id.EndDateEditText);
                results = edittext.getText().toString();
                break;
            case "notes":
                edittext = findViewById(R.id.notesEditMultiLine);
                results = edittext.getText().toString();
                break;
            case "startreminder":
                chk = (CheckBox)  findViewById(R.id.reminderStartDatechk);
                if (chk.isChecked()){
                    results = "Yes";
                }else{
                    results = "No";
                }
                break;
            case "endreminder":
                chk = (CheckBox)  findViewById(R.id.reminderEndDateChk);
                if (chk.isChecked()){
                    results = "Yes";
                }else{
                    results = "No";
                }

        }
        return results;
    }

}