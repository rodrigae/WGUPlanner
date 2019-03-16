package com.example.wguplanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import Utilities.TermData;
import models.Term;
import wguplanner_details.TermDetailsActivity;

public class TermActivity extends MainActivity {

    ArrayAdapter<String> TermAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.app_bar_main_term, null, false);
        drawer.addView(contentView,0);
        fab = (FloatingActionButton) findViewById(R.id.addTerm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermActivity.this, TermDetailsActivity.class);
                startActivity(intent);
            }
        });

        //edit the term
        final ListView terms = findViewById(R.id.termListView);
        terms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //Load the term into the TermDetailsActivity
                    String termName = terms.getItemAtPosition(position).toString();
                    termName = termName.substring(0,termName.indexOf("\n")).trim();
                    Intent intent = new Intent(TermActivity.this, TermDetailsActivity.class);
                    intent.putExtra("Term", termName);
                    startActivity(intent);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

       LoadList();

    }

    private void LoadList(){
        //load the term list
        try {
            //get the list
            ListView termListAdpt = findViewById(R.id.termListView);
            //set the adapter for term list
            TermAdapter = new ArrayAdapter<String>(TermActivity.this, android.R.layout.simple_list_item_1, TermData.getTermbyNamesWithDates());
            termListAdpt.setAdapter(TermAdapter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

 }