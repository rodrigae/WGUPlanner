package com.example.wguplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import Utilities.CourseData;
import Utilities.MentorData;
import wguplanner_details.CourseDetailsActivity;
import wguplanner_details.MentorDetailsActivity;

public class MentorActivity extends MainActivity {
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.app_bar_main_mentors, null, false);
       // drawer.addView(contentView, 0);
        drawer.addView(contentView,0);
        fab = (FloatingActionButton) findViewById(R.id.addMentor);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MentorActivity.this, MentorDetailsActivity.class));
            }
        });

        //edit the term
        final ListView Mentor = findViewById(R.id.mentorListView);
        Mentor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //Load the course into CourseDetails
                    String MentorName = Mentor.getItemAtPosition(position).toString();
                    Intent intent = new Intent(MentorActivity.this, MentorDetailsActivity.class);
                    intent.putExtra("Mentor", MentorName);
                    startActivity(intent);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        LoadList();
    }

    private void LoadList(){
        //load the Course list
        try {
            ListView MentorListAdpt = findViewById(R.id.mentorListView);
            //set the adapter for list
            adapter = new ArrayAdapter<String>(MentorActivity.this, android.R.layout.simple_list_item_1, MentorData.getMentorsbyNames());
            MentorListAdpt.setAdapter(adapter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}