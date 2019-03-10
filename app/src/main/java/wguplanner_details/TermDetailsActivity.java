package wguplanner_details;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.wguplanner.R;
import com.example.wguplanner.MainActivity;
import com.example.wguplanner.TermActivity;

import Database.dbSqlLiteManager;
import Database.dbTermStatements;
import Utilities.TermData;
import models.Term;

public class TermDetailsActivity extends MainActivity {

private Term term = null;
    private EditText edittext = null;
    private CheckBox chk = null;
    private RecyclerView list = null;
    private String msg = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.app_main_term_detail, null, false);
        drawer.addView(contentView,0);

        //obtain access from the database
        database = new dbSqlLiteManager(this).getWritableDatabase();

        fab = (FloatingActionButton) findViewById(R.id.saveTerm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                term = new Term(getData("startreminder"),getData("endreminder"),getData("termTitle") ,getData("startdate") , getData("enddate"), getData("notes"), getData("assessmentId"),getData("MentorId"));

             if (validateEntry()) {
                    if (dbTermStatements.SaveTermDetails(term, database)) {
                        startActivity(new Intent(TermDetailsActivity.this, TermActivity.class));
                    } else {
                        Snackbar.make(view, "Failed Saving New Term", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }else{
                    Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }


            }
        });
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
        }else if (TermData.getTermList().containsKey(term.getTitle())){
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
            case "termTitle":
                edittext = findViewById(R.id.TermTitleEditText);
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