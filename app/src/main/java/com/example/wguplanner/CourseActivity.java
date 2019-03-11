package com.example.wguplanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
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
       // drawer.addView(contentView, 0);
        drawer.addView(contentView,0);
        fab = (FloatingActionButton) findViewById(R.id.addCourse);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CourseActivity.this, CourseDetailsActivity.class));
                           }
        });

        //edit the term
        final ListView Course = findViewById(R.id.courseListView);
        Course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //Load the course into CourseDetails
                    String CourseName = Course.getItemAtPosition(position).toString();
                    Intent intent = new Intent(CourseActivity.this, CourseDetailsActivity.class);
                    intent.putExtra("Assessment", CourseName);
                    startActivity(intent);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        LoadCourseList();
    }

    private void LoadCourseList(){
        //load the Course list
        Cursor c = null;
        TreeMap<String, Course> data = new TreeMap<>();
        try {
            Course CourseItem = null;
            //get the list
            ListView CourseListAdpt = findViewById(R.id.courseListView);
            //set the adapter for list
            Collections.sort(CourseData.getCoursesbyNames());
            adapter = new ArrayAdapter<String>(CourseActivity.this, android.R.layout.simple_list_item_1, CourseData.getCoursesbyNames());
            CourseListAdpt.setAdapter(adapter);
            database.close();
        }catch(Exception e){
            if (e.getMessage().contains("no such table")){
                database.execSQL(dbCourse.CREATE_TABLE);
            }
            e.printStackTrace();
        }
    }


}