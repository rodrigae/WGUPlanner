package com.example.wguplanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import Database.dbCourse;
import Database.dbSqlLiteManager;
import Utilities.CourseData;
import models.Course;
import wguplanner_details.AssessmentDetailsActivity;
import wguplanner_details.CourseDetailsActivity;
import wguplanner_details.TermDetailsActivity;

public class CourseActivity extends MainActivity {
    ArrayList<String> items = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.app_main_course, null, false);
        background = findViewById(R.id.main_logo);
        background.setVisibility(View.GONE);
        drawer.addView(contentView,0);

        //edit the term
        final ListView Course = findViewById(R.id.courseListView);
        Course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //Load the course into CourseDetails
                    String CourseName = Course.getItemAtPosition(position).toString();
                    CourseName = CourseName.substring(0,CourseName.indexOf("\n")).trim();
                    Intent intent = new Intent(CourseActivity.this, CourseDetailsActivity.class);
                    intent.putExtra("Assessment", CourseName);
                    startActivity(intent);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        LoadList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_toolbar, menu);
        MenuItem item = menu.findItem(R.id.share);
        item.setVisible(false);
        item = menu.findItem(R.id.action_save);
        item.setVisible(false);
        item = menu.findItem(R.id.action_delete);
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
        if (id == R.id.action_add) {
            startActivity(new Intent(CourseActivity.this, CourseDetailsActivity.class));
        }
        return true;
    }


    private void LoadList(){
        //load the Course list
        try {
            ListView CourseListAdpt = findViewById(R.id.courseListView);
            //set the adapter for list
            adapter = new ArrayAdapter<String>(CourseActivity.this, android.R.layout.simple_list_item_1, CourseData.getCoursesbyNamesWithDates());
            CourseListAdpt.setAdapter(adapter);
           }catch(Exception e){
           e.printStackTrace();
        }
    }


}