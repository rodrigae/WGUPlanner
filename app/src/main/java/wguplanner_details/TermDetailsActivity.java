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
import com.example.wguplanner.R;
import com.example.wguplanner.MainActivity;
import com.example.wguplanner.TermActivity;

import java.util.ArrayList;

import Database.dbAssignedCourse;
import Database.dbSqlLiteManager;
import Database.dbStatements;
import Utilities.TermData;
import models.Term;

public class TermDetailsActivity extends MainActivity {

    private Term term = null;
    private EditText edittext = null;
    private CheckBox chk = null;
    private String msg = null;
    private ArrayList<String> assignedItem = new ArrayList<>();
    private ArrayList<String> AvailableItems =new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> availableCourseadapter;
    private View contentView = null;
    private String TermName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.app_main_term_detail, null, false);
        drawer.addView(contentView,0);


        //obtain access from the database
        database = new dbSqlLiteManager(this).getWritableDatabase();
        //load the data if the user is attempting to edit an item.
        TermName = getIntent().getStringExtra("Term");
        if (TermName != null){

            setTheFields();
            LoadAssignedCourseList();
        }

        LoadAvailableCourseList();

        //move courses to the assigned list
        final ListView CourseListAvailable = findViewById(R.id.TermavailableCourseList);
        CourseListAvailable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               AssignCourseToTerm(view, position);
            }
        });

        //remove courses from the assignedList
        final ListView CourseListAssigned = findViewById(R.id.TermassignedCourseList);
        CourseListAssigned.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               RemoveAssignedCourse(view, position);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so lon
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            term = new Term(getData("termTitle") ,getData("startdate") , getData("enddate"), getData("notes"));

            if (validateEntry()) {

                database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                boolean update = TermName != null; //is this an update or not, we do this by checking if a Term Name was passed to this Activity

                //for updates
                if (update){
                    //delete the term from term table and assignedcourse
                    dbStatements.deleteTerm(TermName, database);
                    database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                    dbStatements.deleteAssignedCourses(TermName, database);
                }
                //save the term data
                database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                boolean saveTerm = dbStatements.SaveTermDetails(term, database);

                //save the assigned course
                database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                boolean saveAssignedCourse = dbStatements.SaveAssignedTermCourse(term, database, assignedItem);

                if (saveTerm && saveAssignedCourse) {
                    startActivity(new Intent(TermDetailsActivity.this, TermActivity.class));
                } else {
                    Snackbar.make(contentView, "Term Saved: " + saveTerm + " Courses Saved: " + saveAssignedCourse, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }else{
                Snackbar.make(contentView, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }




        }

        if (id == R.id.action_delete){
            database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
            boolean update = TermName != null; //is this an update or not, we do this by checking if a Term Name was passed to this Activity
            //for updates
            if (update){
                if (assignedItem.isEmpty()) {
                    //delete the term from term table and assignedcourse
                    dbStatements.deleteTerm(TermName, database);
                    database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                    dbStatements.deleteAssignedCourses(TermName, database);
                    startActivity(new Intent(TermDetailsActivity.this, TermActivity.class));
                }else{
                    Snackbar.make(contentView, "Please remove all assigned courses before deleting this term.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }else{
                Snackbar.make(contentView, "You cannot delete a new creation", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_toolbar, menu);
        return true;
    }

    private boolean validateEntry() {
        //make sure these fields are not empty before proceeding
        if (term.getTitle().isEmpty()){
            msg = "Title is empty";
            setFocusOnField("termTitle");
            return false;
        }else if (TermData.getTermList().containsKey(term.getTitle()) && TermName == null){
            msg = term.getTitle() + " already exists. ";
            setFocusOnField("termTitle");
            return false;
        }else if (term.getStartDate().isEmpty()){
            msg = "Start date is empty";
            setFocusOnField("startdate");
            return false;
        }else if (term.getEndDate().isEmpty()){
            msg = "End date is empty";
            setFocusOnField("enddate");
            return false;
        }else if (term.getStartDate().length() != 10){
            msg = "wrong format date, please use mm/dd/yyyy";
            setFocusOnField("startdate");
            return false;
        }else if (term.getEndDate().length() != 10){
            msg = "wrong format date, please use mm/dd/yyyy";
            setFocusOnField("enddate");
            return false;
        }
        return true;

    }
    private void setFocusOnField(String field){
        switch (field) {
            case "termTitle":
                edittext = findViewById(R.id.TermTitleEditText);
                edittext.requestFocus();
                break;
            case "startdate":
                edittext = findViewById(R.id.TermStartDateEditText);
                edittext.requestFocus();
                break;
            case "enddate":
                edittext = findViewById(R.id.TermEndDateEditText);
                edittext.requestFocus();
                break;

        }
    }

    private void setTheFields(){
        try {   //set the fields for edit.
                Term data = TermData.getTermList().get(TermName);
                //set the title
                edittext = findViewById(R.id.TermTitleEditText);
                edittext.setText(data.getTitle());
                //set the start date
                edittext = findViewById(R.id.TermStartDateEditText);
                edittext.setText(data.getStartDate());
                //set the end date
                edittext = findViewById(R.id.TermEndDateEditText);
                edittext.setText(data.getEndDate());
                //set the notes section
                edittext = findViewById(R.id.TermnotesEditMultiLine);
                edittext.setText(data.getNotes());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private String getData(String item) {
        String results = null;

        switch (item) {
            case "termTitle":
                edittext = findViewById(R.id.TermTitleEditText);
                results = edittext.getText().toString();
                break;
            case "startdate":
                edittext = findViewById(R.id.TermStartDateEditText);
                results = edittext.getText().toString();
                break;
            case "enddate":
                edittext = findViewById(R.id.TermEndDateEditText);
                results = edittext.getText().toString();
                break;
            case "notes":
                edittext = findViewById(R.id.TermnotesEditMultiLine);
                results = edittext.getText().toString();
                break;

        }
        return results;
    }
    private void LoadAvailableCourseList(){
        //load the Course list
        Cursor c = null;
        try {
            //get the list
            ListView CourseListAdpt = findViewById(R.id.TermavailableCourseList);
            //set the adapter for list
            AvailableItems = new ArrayList<>();
            // get the database access
            database = new dbSqlLiteManager(this).getReadableDatabase(); //get the database access
            c = database.rawQuery("SELECT title FROM course ", null);
            adapter = new ArrayAdapter<String>(TermDetailsActivity.this, android.R.layout.simple_list_item_1, AvailableItems);
            if (c.moveToFirst()) {
                do {
                    if (!assignedItem.contains(c.getString(0))) {
                        AvailableItems.add(c.getString(0));// Add items to the adapter
                    }
                } while (c.moveToNext());

            }

            CourseListAdpt.setAdapter(adapter);
            database.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void LoadAssignedCourseList(){
        //load the Course list assigned
        Cursor c = null;
        try {
            //get the list
            ListView CourseListAdpt = findViewById(R.id.TermassignedCourseList);
            //set the adapter for list
            // get the database access
            assignedItem = new ArrayList<>();
            database = new dbSqlLiteManager(this).getReadableDatabase(); //get the database access
            c = database.rawQuery("SELECT course FROM assignedcourse WHERE title = '" + TermName+"'", null);
            adapter = new ArrayAdapter<String>(TermDetailsActivity.this, android.R.layout.simple_list_item_1, assignedItem);
            if (c.moveToFirst()) {
                do {


                    assignedItem.add(c.getString(0));// Add items to the adapter
                } while (c.moveToNext());

            }
            CourseListAdpt. setAdapter(adapter);
            database.close();
        }catch(Exception e){
            if (e.getMessage().contains("no such table")){
                database = new dbSqlLiteManager(this).getWritableDatabase();
                database.execSQL(dbAssignedCourse.CREATE_TABLE);
            }
            e.printStackTrace();
        }
    }
    private void AssignCourseToTerm(View view, int position){
    //add and remove items from assign and available list of courses
    ListView CourseListAssigned = findViewById(R.id.TermassignedCourseList);
    ListView CourseListAvailable = findViewById(R.id.TermavailableCourseList);
    try {
        if (position > -1) {
            String CourseName = CourseListAvailable.getItemAtPosition(position).toString();
            if (!CourseName.isEmpty() && !assignedItem.contains(CourseName)) {
                //assign the course name to the list
                adapter = new ArrayAdapter<String>(TermDetailsActivity.this, android.R.layout.simple_list_item_1, assignedItem);
                assignedItem.add(CourseName);
                AvailableItems.remove(CourseName);//remove from the available list
                CourseListAssigned.setAdapter(adapter);

                //remove the assigned course from the available list
                adapter = new ArrayAdapter<String>(TermDetailsActivity.this, android.R.layout.simple_list_item_1, AvailableItems);
                CourseListAvailable.setAdapter(adapter);
            }
            Snackbar.make(contentView, CourseName + " Assigned", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }catch(Exception e){
        e.printStackTrace();
    }
}
    private void RemoveAssignedCourse(View view, int position){
        //add and remove items from assign and available list of courses
        ListView CourseListAssigned = findViewById(R.id.TermassignedCourseList);
        ListView CourseListAvailable = findViewById(R.id.TermavailableCourseList);
        try {
            if (position > -1) {
                String CourseName = CourseListAssigned.getItemAtPosition(position).toString();
                if (!CourseName.isEmpty() && !AvailableItems.contains(CourseName)) {
                    //remove the assigned course from the available list
                    adapter = new ArrayAdapter<String>(TermDetailsActivity.this, android.R.layout.simple_list_item_1,   AvailableItems);
                    AvailableItems.add(CourseName);
                    assignedItem.remove(CourseName);  //remove from the available list
                    CourseListAvailable.setAdapter(adapter);

                    //assign course name back to the available list
                    adapter = new ArrayAdapter<String>(TermDetailsActivity.this, android.R.layout.simple_list_item_1, assignedItem);
                    CourseListAssigned.setAdapter(adapter);


                }
                Snackbar.make(contentView, CourseName + " Removed", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}