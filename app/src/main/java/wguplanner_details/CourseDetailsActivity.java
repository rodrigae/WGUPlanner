package wguplanner_details;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.example.wguplanner.CourseActivity;
import com.example.wguplanner.MainActivity;
import com.example.wguplanner.R;
import com.example.wguplanner.CourseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Database.dbAssignedAssessment;
import Database.dbAssignedCourse;
import Database.dbSqlLiteManager;
import Database.dbStatements;
import Utilities.CourseData;
import Utilities.TermData;
import models.Course;
import Database.dbAssesssment;
import models.Course;

public class CourseDetailsActivity extends MainActivity {
    //items for reuse
    private Course Course = null;
    private EditText edittext = null;
    private CheckBox chk = null;
    private String msg = null;
    private String CourseName = "";


    private ArrayAdapter<String> listViewAdapter;
    ListView AssessmentListAssigned = null;
    ListView AssessmentListAvailable = null;

    private static ArrayList<String> assignedAssessmentItem =null;
    private static ArrayList<String> AvailableAssessmentItems = null;


    //view is used for snackbar notification
    private View contentView = null;

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

        //load the data if the user is attempting to edit an item.
        CourseName = getIntent().getStringExtra("Assessment");
        if (CourseName != null){

            setTheFieldsForEditing();
            LoadAssignedAssessmentList();
        }

        LoadAvailableAssessmentList();

        //move courses to the assigned list
        final ListView AssessmentListAvailable = findViewById(R.id.CourseAvailableAssessmentList);
        AssessmentListAvailable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AssignAssessmentToCourse(view, position);
            }
        });

        //remove courses from the assignedList
        final ListView AssessmentListAssigned = findViewById(R.id.CourseAssignedAssessmentList);
        AssessmentListAssigned.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RemoveAssignedAssessment(view, position);
            }
        });

        Snackbar.make(contentView, "Select item from list to assign or un-assigned", Snackbar.LENGTH_LONG).setAction("Action", null).show();

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

                        database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                        boolean update = CourseName != null; //is this an update or not, we do this by checking if a Course Name was passed to this Activity

                        //for updates
                        if (update){
                            //delete the course from course table and assignedAssessment
                            dbStatements.deleteCourse(CourseName, database);
                            database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                            dbStatements.deleteAssignedAssessment(CourseName, database);
                        }

                        //save the Course data
                        database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                        boolean saveCourse = dbStatements.SaveCourseDetails(Course, database);

                        //save the assigned course
                        database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                        boolean saveAssignedAssessment = dbStatements.SaveAssignedCourseAssessment(Course, database, assignedAssessmentItem);


                        if (saveCourse && saveAssignedAssessment) {
                            startActivity(new Intent(CourseDetailsActivity.this, CourseActivity.class));
                        } else {
                            Snackbar.make(contentView, "Course Saved: " + saveCourse + " Assessment Saved: " + saveAssignedAssessment, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }else{
                        Snackbar.make(contentView, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
        }

        if (id == R.id.action_delete) {
            database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
            boolean update = CourseName != null; //is this an update or not, we do this by checking if a Course Name was passed to this Activity
            //for updates
            if (update) {
                //check to see if the course is being use in a term before deleting
                List<String> termsList = new ArrayList<>();
                String TermName = null;
                List<String> listofTerms = null;

                for (String Name : TermData.getAssignCourseList().keySet()){
                        listofTerms = TermData.getAssignCourseList().get(Name);
                    if (listofTerms.contains(CourseName)){
                        termsList.add(Name);
                    }
                }

                if (termsList.isEmpty()) {
                    if (assignedAssessmentItem.isEmpty()) {
                        //delete the Course from Course table and assignedcourse
                        dbStatements.deleteCourse(CourseName, database);
                        database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                        dbStatements.deleteAssignedAssessment(CourseName, database);
                        startActivity(new Intent(CourseDetailsActivity.this, CourseActivity.class));
                    } else {
                        Snackbar.make(contentView, "Please remove all assigned assessment before deleting this Course.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }else{
                    Snackbar.make(contentView, "This course is being used in term/s: " + termsList , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }else {
                    Snackbar.make(contentView, "You cannot delete a new creation", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }


        }
            return true;
        }



    //validates user entries
    private boolean validateEntry() {
        //make sure these fields are not empty before proceeding
        if (Course.getTitle().isEmpty()){
            msg = "Title is empty";
            // setFocusOnField("CourseTitle");
            edittext = findViewById(R.id.CourseTitleEditText); edittext.requestFocus();
            return false;
        }else if (CourseData.getCoursesbyNames().contains(Course.getTitle()) && CourseName == null){
            msg = Course.getTitle() + " already exists. ";
            //setFocusOnField("CourseTitle");
            edittext = findViewById(R.id.CourseTitleEditText); edittext.requestFocus();
            return false;
        }else if (Course.getStartDate().isEmpty()){
            msg = "Start date is empty";
            //setFocusOnField("startdate");
            edittext = findViewById(R.id.CourseStartDateEditText); edittext.requestFocus();
            return false;
        }else if (Course.getEndDate().isEmpty()){
            msg = "End date is empty";
            //setFocusOnField("enddate");
            edittext = findViewById(R.id.CourseEndDateEditText); edittext.requestFocus();
            return false;
        }else if (Course.getStartDate().length() != 10){
            msg = "wrong format date, please use mm/dd/yyyy";
            //setFocusOnField("startdate");
            edittext = findViewById(R.id.CourseStartDateEditText);  edittext.requestFocus();
            return false;
        }else if (Course.getEndDate().length() != 10){
            msg = "wrong format date, please use mm/dd/yyyy";
            //setFocusOnField("enddate");
            edittext = findViewById(R.id.CourseEndDateEditText); edittext.requestFocus();
            return false;
        }
        return true;

    }


    private void setTheFieldsForEditing(){
        try {   //set the fields for edit.
            Course data = CourseData.getCreatedCourse().get(CourseName);
            //set the title
            edittext = findViewById(R.id.CourseTitleEditText);
            edittext.setText(data.getTitle());
            //set the start date
            edittext = findViewById(R.id.CourseStartDateEditText);
            edittext.setText(data.getStartDate());
            //set the end date
            edittext = findViewById(R.id.CourseEndDateEditText);
            edittext.setText(data.getEndDate());
            //set the notes section
            edittext = findViewById(R.id.CourseNotesEditMultiLine);
            edittext.setText(data.getNotes());
            //set the reminders for start date
            chk = findViewById(R.id.CourseReminderStartDatechk);
            if (data.getReminderStartDate().equals("Yes")){
                chk.setChecked(true);
            }else{
                chk.setChecked(false);
            }
            //set the reminders for end date
            chk = findViewById(R.id.CourseReminderEndDateChk);
            if (data.getIsReminderEndDate().equals("Yes")){
                chk.setChecked(true);
            }else{
                chk.setChecked(false);
            }

        }catch(Exception e){
            e.printStackTrace();
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
                edittext = findViewById(R.id.CourseStartDateEditText);
                results = edittext.getText().toString();
                break;
            case "enddate":
                edittext = findViewById(R.id.CourseEndDateEditText);
                results = edittext.getText().toString();
                break;
            case "notes":
                edittext = findViewById(R.id.CourseNotesEditMultiLine);
                results = edittext.getText().toString();
                break;
            case "startreminder":
                chk = (CheckBox)  findViewById(R.id.CourseReminderStartDatechk);
                if (chk.isChecked()){
                    results = "Yes";
                }else{
                    results = "No";
                }
                break;
            case "endreminder":
                chk = (CheckBox)  findViewById(R.id.CourseReminderEndDateChk);
                if (chk.isChecked()){
                    results = "Yes";
                }else{
                    results = "No";
                }

        }
        return results;
    }

    private void LoadAvailableAssessmentList(){
        //load the Course list
        Cursor c = null;
        try {
            //get the list
            ListView AssessmentListAdpt = findViewById(R.id.CourseAvailableAssessmentList);
            Collections.sort(CourseData.getAvailableAssessmentItems());
           // get the database access
           listViewAdapter = new ArrayAdapter<String>(CourseDetailsActivity.this, android.R.layout.simple_list_item_1, CourseData.getAvailableAssessmentItems());
            AssessmentListAdpt.setAdapter(listViewAdapter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void LoadAssignedAssessmentList(){
        //load the Course list assigned
        Cursor c = null;
        try {
            //get the list
            ListView AssessmentListAdpt = findViewById(R.id.CourseAssignedAssessmentList);
            //set the adapter for list
            // get the database access
            assignedAssessmentItem = new ArrayList<>();
            database = new dbSqlLiteManager(this).getReadableDatabase(); //get the database access
            c = database.rawQuery("SELECT "+ dbAssignedAssessment.COLUMN_ASSESSMENTNAME+" FROM "+dbAssignedAssessment.TABLE_NAME+" WHERE "+dbAssignedAssessment.COLUMN_COURSENAME+" = '" +CourseName+"'", null);
            listViewAdapter = new ArrayAdapter<String>(CourseDetailsActivity.this, android.R.layout.simple_list_item_1,assignedAssessmentItem);
            if (c.moveToFirst()) {
                do {
                    assignedAssessmentItem.add(c.getString(0));// Add items to the adapter
                } while (c.moveToNext());

            }
            AssessmentListAdpt. setAdapter(listViewAdapter);
            database.close();
        }catch(Exception e){
            if (e.getMessage().contains("no such table")){
                database = new dbSqlLiteManager(this).getWritableDatabase();
                database.execSQL(dbAssignedAssessment.CREATE_TABLE);
            }
            e.printStackTrace();
        }
    }

    private void AssignAssessmentToCourse(View view, int position){
        //add and remove items from assign and available list of assessment
        AssessmentListAssigned = findViewById(R.id.CourseAssignedAssessmentList);
        AssessmentListAvailable = findViewById(R.id.CourseAvailableAssessmentList);
        try {
            if (position > -1) {
                String AssessmentName = AssessmentListAssigned.getItemAtPosition(position).toString();
                if (!AssessmentName.isEmpty() && !assignedAssessmentItem.contains(AssessmentName)) {
                    //assign the course name to the list
                    listViewAdapter = new ArrayAdapter<String>(CourseDetailsActivity.this, android.R.layout.simple_list_item_1, assignedAssessmentItem);
                    assignedAssessmentItem.add(AssessmentName);
                    AvailableAssessmentItems.remove(AssessmentName);//remove from the available list
                    AssessmentListAssigned.setAdapter(listViewAdapter);

                    //remove the assigned course from the available list
                    listViewAdapter = new ArrayAdapter<String>(CourseDetailsActivity.this, android.R.layout.simple_list_item_1,AvailableAssessmentItems);
                    AssessmentListAvailable.setAdapter(listViewAdapter);
                }
                Snackbar.make(contentView, AssessmentName + " Assigned", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void RemoveAssignedAssessment(View view, int position){
        //add and remove items from assign and available list of assessment
        AssessmentListAssigned = findViewById(R.id.CourseAssignedAssessmentList);
        AssessmentListAvailable = findViewById(R.id.CourseAvailableAssessmentList);
        try {
            if (position > -1) {
                String AssessmentName = AssessmentListAssigned .getItemAtPosition(position).toString();
                if (!AssessmentName.isEmpty() && !AvailableAssessmentItems.contains(AssessmentName)) {
                    //remove the assigned course from the available list
                    listViewAdapter = new ArrayAdapter<String>(CourseDetailsActivity.this, android.R.layout.simple_list_item_1,AvailableAssessmentItems);
                    AvailableAssessmentItems.add(AssessmentName);
                    assignedAssessmentItem.remove(AssessmentName);  //remove from the available list
                    AssessmentListAvailable.setAdapter(listViewAdapter);

                    //assign course name back to the available list
                    listViewAdapter = new ArrayAdapter<String>(CourseDetailsActivity.this, android.R.layout.simple_list_item_1,assignedAssessmentItem);
                    AssessmentListAssigned.setAdapter(listViewAdapter);
                }
                Snackbar.make(contentView, AssessmentName + " Removed", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}