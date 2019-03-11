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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.TreeMap;

import Database.dbCourse;
import Database.dbSqlLiteManager;
import Utilities.CourseData;
import models.Course;
import wguplanner_details.CourseDetailsActivity;
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

        // drawer.setLayoutParams(null);
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

            //get the database access
            database = new dbSqlLiteManager(this).getReadableDatabase(); //get the database access
            c = database.rawQuery("SELECT title,start_date,end_date,course_starts,course_ends,notes,assesssment_id,mentor_id FROM course ", null);
            adapter = new ArrayAdapter<String>(CourseActivity.this, android.R.layout.simple_list_item_1, items);
            if (c.moveToFirst()) {
                do {
                    CourseItem = new Course(c.getString(3), c.getString(4), c.getString(0), c.getString(1),
                            c.getString(2), c.getString(5), c.getString(6), c.getString(7));
                    items.add(c.getString(0));// Add items to the adapter
                    data.put(c.getString(0),CourseItem);//add the list of items to the treemap for later retrival
                } while (c.moveToNext());

            }
            CourseData.setCourseList(data);
            CourseListAdpt. setAdapter(adapter);
            database.close();
        }catch(Exception e){
            if (e.getMessage().contains("no such table")){
                database.execSQL(dbCourse.CREATE_TABLE);
            }
            e.printStackTrace();
        }
    }


}