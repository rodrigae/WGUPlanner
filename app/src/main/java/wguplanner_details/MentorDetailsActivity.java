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
import android.widget.EditText;
import android.widget.ListView;

import com.example.wguplanner.MentorActivity;
import com.example.wguplanner.R;
import com.example.wguplanner.MainActivity;

import java.util.ArrayList;

import Database.dbSqlLiteManager;
import Database.dbStatements;
import Utilities.AssessmentData;
import Utilities.MentorData;
import models.Mentor;

public class MentorDetailsActivity extends MainActivity {
    //items for reuse
    private Mentor mentor = null;
    private EditText edittext = null;
    private CheckBox chk = null;
    private String msg = null;
    private String mentorName = "";
    private View contentView = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        contentView = inflater.inflate(R.layout.app_main_mentor_detail, null, false);
        background = findViewById(R.id.main_logo);
        background.setVisibility(View.GONE);
        drawer.addView(contentView,0);


        mentorName = getIntent().getStringExtra("Mentor");
        if (mentorName != null){
            setTheFieldsForEditing();
        }else{
            edittext = findViewById(R.id.mentorName);
            edittext.requestFocus();
        }

        LoadWhereUsedMentorList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_toolbar, menu);
        MenuItem item = menu.findItem(R.id.share);
        item.setVisible(false);
        item = menu.findItem(R.id.action_add);
        item.setVisible(false);
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
            mentor = new Mentor(getData("name"),getData("email"),getData("phone"));
            if (validateEntry()) {

                database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                boolean update = mentorName != null; //is this an update or not, we do this by checking if a Course Name was passed to this Activity

                //for updates
                if (update){
                    //delete the course from course table and assignedMentor
                    dbStatements.deleteMentor(mentorName, database);
                    database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                }

                //save the assesment data
                database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                boolean saveMentor = dbStatements.SaveMentorDetails(mentor, database);

                if (saveMentor) {
                    MentorData.AddCreatedMentor(mentor.getName(),mentor);
                    ReloadData();
                    startActivity(new Intent(MentorDetailsActivity.this, MentorActivity.class));
                } else {
                    Snackbar.make(contentView, " Mentor Saved: " + saveMentor, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }else{
                Snackbar.make(contentView, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }

        if (id == R.id.action_delete) {
            database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
            boolean update = mentorName != null; //is this an update or not, we do this by checking if a Mentor Name was passed to this Activity
            //for updates
            if (update) {
                //check to see if the course is being use in a term before deleting
                ArrayList<String> whereUsed = MentorData.getWhereUsedMentor(mentorName);
                if (whereUsed == null || whereUsed.isEmpty()) {
                    //delete the Course from Course table and assignedcourse
                    dbStatements.deleteMentor(mentorName, database);
                    database = new dbSqlLiteManager(this).getWritableDatabase();//Re-open the connection, it is being closed at the previous statement
                    ReloadData();

                    startActivity(new Intent(MentorDetailsActivity.this, MentorActivity.class));
                }else{
                    Snackbar.make(contentView, "This mentor is being used in course/s. " + whereUsed , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }else {
                Snackbar.make(contentView, "You cannot delete a new creation", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }


        }
        return true;
    }


    private void setTheFieldsForEditing(){
        try {   //set the fields for edit.
            Mentor data = MentorData.getCreatedMentor().get(mentorName);
            //set the title
            edittext = findViewById(R.id.mentorName);
            edittext.setText(data.getName());

            edittext = findViewById(R.id.mentorEmail);
            edittext.setText(data.getEmail());

            edittext = findViewById(R.id.mentorPhone);
            edittext.setText(data.getPhone());

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void LoadWhereUsedMentorList(){
        //load the Course list, to asign to assessment
        try {
            //get the list
            ListView MentorLists = findViewById(R.id.MentorWhereUsedList);
            ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(MentorDetailsActivity.this, android.R.layout.simple_list_item_1, MentorData.getWhereUsedMentor(mentorName));
            MentorLists.setAdapter(listViewAdapter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //validates user entries
    private boolean validateEntry() {
        //make sure these fields are not empty before proceeding
        if (mentor.getName().isEmpty()) {
            msg = "Name is empty";
            edittext = findViewById(R.id.mentorName);
            edittext.requestFocus();
            return false;
        } else if (MentorData.getMentorsbyNames().contains(mentor.getName()) && mentorName == null) {
            msg = mentor.getName() + " already exists. ";
            edittext = findViewById(R.id.mentorName);
            edittext.requestFocus();
            return false;
        } else if (mentor.getEmail().isEmpty()) {
            msg = "Email is empty";
            edittext = findViewById(R.id.mentorEmail);
            edittext.requestFocus();
            return false;
        } else if (mentor.getPhone().isEmpty()) {
            msg = "Phone is empty";
            edittext = findViewById(R.id.mentorPhone);
            edittext.requestFocus();
            return false;
        }
        return true;

    }

    private String getData(String item) {
        String results = null;

        switch (item) {
            case "name":
                edittext = findViewById(R.id.mentorName);
                results = edittext.getText().toString();
                break;
            case "email":
                edittext = findViewById(R.id.mentorEmail);
                results = edittext.getText().toString();
                break;
            case "phone":
                edittext = findViewById(R.id.mentorPhone);
                results = edittext.getText().toString();
                break;
        }
        return results;
    }
}