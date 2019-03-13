package wguplanner_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import com.example.wguplanner.AssessmentActivity;
import com.example.wguplanner.R;
import com.example.wguplanner.MainActivity;

import java.util.ArrayList;

import Database.dbSqlLiteManager;
import Database.dbStatements;
import Utilities.AssessmentData;
import Utilities.CourseData;
import models.Assessment;

public class AssessmentDetailsActivity extends MainActivity {
    //items for reuse
    private Assessment assessment = null;
    private EditText edittext = null;
    private CheckBox chk = null;
    private String msg = null;
    private String assessmentName = "";

    //view is used for snackbar notification
    private View contentView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.app_main_assessment_detail, null, false);
        // drawer.addView(contentView, 0);
        drawer.addView(contentView,0);

        database = new dbSqlLiteManager(this).getWritableDatabase();

       assessmentName = getIntent().getStringExtra("Assessment");
        if (assessmentName != null){
            setTheFieldsForEditing();
        }
        chk = findViewById(R.id.AssessmentObjective);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //uncheck performance if objective is checked
           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    if (isChecked){
                        chk =  findViewById(R.id.AssessmentPerformance);
                        chk.setChecked(false);
                    }
                }
            }
        );

        chk = findViewById(R.id.AssessmentPerformance);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        //uncheck performance if objective is checked
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                      if (isChecked){
                      chk = findViewById(R.id.AssessmentObjective);
                      chk.setChecked(false);
                                 }
                             }
                         }
        );
        LoadWhereUsedCoursesList();

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
            assessment = new Assessment(getData("title"),getData("type"),getData("status"), getData("goaldate"), getData("reminder"));
            if (validateEntry()) {

                database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                boolean update = assessmentName != null; //is this an update or not, we do this by checking if a Course Name was passed to this Activity

                //for updates
                if (update){
                    //delete the course from course table and assignedAssessment
                    dbStatements.deleteAssessment(assessmentName, database);
                    database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement

                }

                //save the assesment data
                database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                boolean saveAssessment = dbStatements.SaveAssessmentDetails(assessment, database);


                if (saveAssessment) {
                    AssessmentData.AddCreatedAssessment(assessment.getName(),assessment);
                    LoadAssessmentList();
                    startActivity(new Intent(AssessmentDetailsActivity.this, AssessmentActivity.class));
                } else {
                    Snackbar.make(contentView, " Assessment Saved: " + saveAssessment, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }else{
                Snackbar.make(contentView, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }

        if (id == R.id.action_delete) {
            database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
            boolean update = assessmentName != null; //is this an update or not, we do this by checking if a Assessment Name was passed to this Activity
            //for updates
            if (update) {
                //check to see if the course is being use in a term before deleting
                 ArrayList<String> whereUsed = AssessmentData.getWhereUsedAssessment(assessmentName);
              if (whereUsed == null) {
                        //delete the Course from Course table and assignedcourse
                    dbStatements.deleteAssessment(assessmentName, database);
                    database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                    LoadAssessmentList();

                        startActivity(new Intent(AssessmentDetailsActivity.this, AssessmentActivity.class));
                }else{
                    Snackbar.make(contentView, "This assessment is being used in course/s. " + whereUsed , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }else {
                Snackbar.make(contentView, "You cannot delete a new creation", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }


        }
        return true;
    }

    private void LoadWhereUsedCoursesList(){
        //load the Course list, to asign to assessment
        try {
            //get the list
            ListView CourseLists = findViewById(R.id.AssessmentWhereUsedList);
            ArrayAdapter<String>  listViewAdapter = new ArrayAdapter<String>(AssessmentDetailsActivity.this, android.R.layout.simple_list_item_1, AssessmentData.getWhereUsedAssessment(assessmentName));
            CourseLists.setAdapter(listViewAdapter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setTheFieldsForEditing(){
        try {   //set the fields for edit.
            Assessment data = AssessmentData.getCreatedAssessment().get(assessmentName);
            //set the title
            edittext = findViewById(R.id.AssessmentTitleEditText);
            edittext.setText(data.getName());
            //set the start date
            edittext = findViewById(R.id.AssessmentnotesEditMultiLine);
            edittext.setText(data.getStatus());
            //set the end date
            edittext = findViewById(R.id.AssessmentEndDateEditText);
            edittext.setText(data.getGoalDate());
            //set the notes section
            chk = findViewById(R.id.AssessmentReminderStartDatechk);
            if (data.isReminderSet().equals("Yes")){
                chk.setChecked(true);
            }else{
                chk.setChecked(false);
            }

            chk = findViewById(R.id.AssessmentObjective);
            if (data.getType().equals("Objective")){
                chk.setChecked(true);
            }

            chk = findViewById(R.id.AssessmentPerformance);
            if (data.getType().equals("Performance")){
                chk.setChecked(true);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //validates user entries
    private boolean validateEntry() {
        //make sure these fields are not empty before proceeding
        if (assessment.getName().isEmpty()) {
            msg = "Title is empty";
            // setFocusOnField("assessmentTitle");
            edittext = findViewById(R.id.AssessmentTitleEditText);
            edittext.requestFocus();
            return false;
        } else if (AssessmentData.getAssessmentsbyNames().contains(assessment.getName()) && assessmentName == null) {
            msg = assessment.getName() + " already exists. ";
            edittext = findViewById(R.id.AssessmentTitleEditText);
            edittext.requestFocus();
            return false;
        } else if (assessment.getStatus().isEmpty()) {
            msg = "Status is empty";
            edittext = findViewById(R.id.AssessmentnotesEditMultiLine);
            edittext.requestFocus();
            return false;
        } else if (assessment.getGoalDate().isEmpty()) {
            msg = "End date is empty";
            edittext = findViewById(R.id.AssessmentEndDateEditText);
            edittext.requestFocus();
            return false;
        } else if (assessment.getGoalDate().length() != 10) {
            msg = "wrong format date, please use mm/dd/yyyy";
            edittext = findViewById(R.id.AssessmentEndDateEditText);
            edittext.requestFocus();
            return false;
        } else if (assessment.getType().isEmpty()) {
            msg = "No type choosen";
            //no focus required since there are two of them.
            return false;
        }
        return true;

    }

    private String getData(String item) {
        String results = null;

        switch (item) {
            case "title":
                edittext = findViewById(R.id.AssessmentTitleEditText);
                results = edittext.getText().toString();
                break;
            case "status":
                edittext = findViewById(R.id.AssessmentnotesEditMultiLine);
                results = edittext.getText().toString();
                break;
            case "goaldate":
                edittext = findViewById(R.id.AssessmentEndDateEditText);
                results = edittext.getText().toString();
                break;
            case "reminder":
                chk = (CheckBox)  findViewById(R.id.AssessmentReminderStartDatechk);
                if (chk.isChecked()){
                    results = "Yes";
                }else{
                    results = "No";
                }
                break;
            case "type":
                chk = findViewById(R.id.AssessmentPerformance);
                if (chk.isChecked()){
                    results = "Performance";
                    return results;
                }

                chk = findViewById(R.id.AssessmentObjective);
                if (chk.isChecked()){
                    results = "Objective";
                    return results;
                }

        }
        return results;
    }
}