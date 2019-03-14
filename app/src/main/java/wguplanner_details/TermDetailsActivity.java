package wguplanner_details;

import android.content.Context;
import android.content.Intent;
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
import java.util.Collections;

import Database.dbSqlLiteManager;
import Database.dbStatements;
import Utilities.CourseData;
import Utilities.TermData;
import models.Term;

public class TermDetailsActivity extends MainActivity {
    //items for reuse
    private Term term = null;
    private EditText edittext = null;
    private CheckBox chk = null;
    private String msg = null;
    private String TermName = "";


    private ArrayAdapter<String> listViewAdapter;
    ListView CourseListAssigned = null;
    ListView CourseListAvailable = null;

    private static ArrayList<String> assignedCourseItem = new ArrayList<>();
    private static ArrayList<String> AvailableCourseItems =null;

    //view is used for snackbar notification
    private View contentView = null;


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
            setTheFieldsForEditing();
            LoadAssignedCourseList();
        }else{
            //clear the list since we are creating something ne
            assignedCourseItem.clear();//clear the items
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

        Snackbar.make(contentView, "Select item from list to assign or un-assigned", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        //assign the items for later use
        if (TermName == null){
            AvailableCourseItems = CourseData.getCoursesbyNames();
        }else {
            assignedCourseItem = TermData.getAssignedCoursesByTerm(TermName);
            AvailableCourseItems = TermData.getAvailableCoursesByTerm(TermName);
        }

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
                boolean saveAssignedCourse = dbStatements.SaveAssignedTermCourse(term, database, assignedCourseItem);

                if (saveTerm && saveAssignedCourse) {
                    TermData.addCreatedTermList(term.getTitle(),term);//this is to update the term list for when term is opened, after refresh
                    if (TermName == null){
                        //tells the system is a new entry
                        TermData.addNewAssignedCourses(term.getTitle(), assignedCourseItem);
                    }
                    LoadCourseList();//Load Course lists, used in TermDetailsActivity, and also CourseActivity
                    LoadTermList();
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
                if (assignedCourseItem.isEmpty()) {
                    //delete the term from term table and assignedcourse
                    dbStatements.deleteTerm(TermName, database);
                    database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                    dbStatements.deleteAssignedCourses(TermName, database);
                    LoadCourseList();//Load Course lists, used in TermDetailsActivity, and also CourseActivity
                    LoadTermList();

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

    //load the customer bar for use
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_toolbar, menu);
        MenuItem share = menu.findItem(R.id.share);
        share.setVisible(false);
        return true;
    }

    //validates user entries
    private boolean validateEntry() {
        //make sure these fields are not empty before proceeding
        if (term.getTitle().isEmpty()){
            msg = "Title is empty";
           // setFocusOnField("termTitle");
            edittext = findViewById(R.id.TermTitleEditText); edittext.requestFocus();
            return false;
        }else if (TermData.getCreatedTermList().containsKey(term.getTitle()) && TermName == null){
            msg = term.getTitle() + " already exists. ";
            //setFocusOnField("termTitle");
            edittext = findViewById(R.id.TermTitleEditText); edittext.requestFocus();
            return false;
        }else if (term.getStartDate().isEmpty()){
            msg = "Start date is empty";
            //setFocusOnField("startdate");
            edittext = findViewById(R.id.TermStartDateEditText); edittext.requestFocus();
            return false;
        }else if (term.getEndDate().isEmpty()){
            msg = "End date is empty";
            //setFocusOnField("enddate");
            edittext = findViewById(R.id.TermEndDateEditText); edittext.requestFocus();
            return false;
        }else if (term.getStartDate().length() != 10){
            msg = "wrong format date, please use mm/dd/yyyy";
            //setFocusOnField("startdate");
            edittext = findViewById(R.id.TermStartDateEditText);  edittext.requestFocus();
            return false;
        }else if (term.getEndDate().length() != 10){
            msg = "wrong format date, please use mm/dd/yyyy";
            //setFocusOnField("enddate");
            edittext = findViewById(R.id.TermEndDateEditText); edittext.requestFocus();
            return false;
        }
        return true;

    }

    private void setTheFieldsForEditing(){
        try {   //set the fields for edit.
                Term data = TermData.getCreatedTermList().get(TermName);
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
         try {
            //get the list
            ListView CourseListAdpt = findViewById(R.id.TermavailableCourseList);
            //set the adapter for list

             ArrayList<String> list = new ArrayList<>();
             //determines if its a new entry here
             if (TermName == null){
                 list = CourseData.getCoursesbyNames();
             }else{
                 list = TermData.getAvailableCoursesByTerm(TermName);
             }

             if (list != null){
                 Collections.sort(list);
             }

            listViewAdapter = new ArrayAdapter<String>(TermDetailsActivity.this, android.R.layout.simple_list_item_1, list);
            CourseListAdpt.setAdapter(listViewAdapter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void LoadAssignedCourseList(){
        //load the Course list assigned

        try {
            //get the list
            ListView CourseListAdpt = findViewById(R.id.TermassignedCourseList);
            //set the adapter for list
               listViewAdapter = new ArrayAdapter<String>(TermDetailsActivity.this, android.R.layout.simple_list_item_1, TermData.getAssignedCoursesByTerm(TermName));
               CourseListAdpt.setAdapter(listViewAdapter);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void AssignCourseToTerm(View view, int position){
    //add and remove items from assign and available list of courses
     CourseListAssigned = findViewById(R.id.TermassignedCourseList);
     CourseListAvailable = findViewById(R.id.TermavailableCourseList);

    try {
        if (position > -1) {
            String CourseName = CourseListAvailable.getItemAtPosition(position).toString();
              if (!CourseName.isEmpty() && !assignedCourseItem.contains(CourseName)) {
                //assign the course name to the list
                listViewAdapter = new ArrayAdapter<String>(TermDetailsActivity.this, android.R.layout.simple_list_item_1, assignedCourseItem);
                assignedCourseItem.add(CourseName);
                AvailableCourseItems.remove(CourseName);//remove from the available list
                CourseListAssigned.setAdapter(listViewAdapter);

                //remove the assigned course from the available list
                listViewAdapter = new ArrayAdapter<String>(TermDetailsActivity.this, android.R.layout.simple_list_item_1, AvailableCourseItems);
                CourseListAvailable.setAdapter(listViewAdapter);
                Snackbar.make(contentView, CourseName + " Assigned", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        }
    }catch(Exception e){
        e.printStackTrace();
    }
}
    private void RemoveAssignedCourse(View view, int position){
        //add and remove items from assign and available list of courses
        CourseListAssigned = findViewById(R.id.TermassignedCourseList);
        CourseListAvailable = findViewById(R.id.TermavailableCourseList);

        try {
            if (position > -1) {
                String CourseName = CourseListAssigned.getItemAtPosition(position).toString();
                 if (!CourseName.isEmpty()) {
                    //remove the assigned course from the available list
                    listViewAdapter = new ArrayAdapter<String>(TermDetailsActivity.this, android.R.layout.simple_list_item_1, AvailableCourseItems);
                    if (!AvailableCourseItems.contains(CourseName)){AvailableCourseItems.add(CourseName);}
                    assignedCourseItem.remove(CourseName);  //remove from the available list
                    CourseListAvailable.setAdapter(listViewAdapter);

                    //assign course name back to the available list
                    listViewAdapter = new ArrayAdapter<String>(TermDetailsActivity.this, android.R.layout.simple_list_item_1, assignedCourseItem);
                    CourseListAssigned.setAdapter(listViewAdapter);
                    Snackbar.make(contentView, CourseName + " Removed", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



}