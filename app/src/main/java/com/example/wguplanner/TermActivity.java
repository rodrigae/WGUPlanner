package com.example.wguplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;

import wguplanner_details.CourseDetailsActivity;
import wguplanner_details.TermDetailsActivity;

public class TermActivity extends MainActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.app_bar_main_term, null, false);
       // drawer.addView(contentView, 0);
        drawer.addView(contentView,0);
        fab = (FloatingActionButton) findViewById(R.id.addTerm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TermActivity.this, TermDetailsActivity.class));
            }
        });

        // drawer.setLayoutParams(null);

    }


}