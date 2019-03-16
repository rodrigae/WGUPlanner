package com.example.wguplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import Utilities.AssessmentData;
import wguplanner_details.AssessmentDetailsActivity;

public class AssessmentActivity extends MainActivity {
    ArrayAdapter<String> AssessmentAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.app_bar_main_assessment, null, false);
       // drawer.addView(contentView, 0);
        drawer.addView(contentView,0);
        fab = (FloatingActionButton) findViewById(R.id.addAssessment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AssessmentActivity.this, AssessmentDetailsActivity.class));
            }
        });

        //edit the AssessmentName
        final ListView assessment = findViewById(R.id.assessmentListView);
        assessment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //Load the AssessmentName into the AssessmentNameDetailsActivity
                    String AssessmentName = assessment.getItemAtPosition(position).toString();
                    Intent intent = new Intent(AssessmentActivity.this, AssessmentDetailsActivity.class);
                    intent.putExtra("Assessment", AssessmentName);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        LoadList();
    }

    private void LoadList() {
        //load the term list
        try {
            //get the list
            ListView termListAdpt = findViewById(R.id.assessmentListView);
            //set the adapter for term list
            AssessmentAdapter = new ArrayAdapter<String>(AssessmentActivity.this, android.R.layout.simple_list_item_1, AssessmentData.getAssessmentsbyNames());
            termListAdpt.setAdapter(AssessmentAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}