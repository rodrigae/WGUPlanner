package com.example.wguplanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.TreeMap;

import Database.dbSqlLiteManager;
import models.Term;
import wguplanner_details.CourseDetailsActivity;
import wguplanner_details.TermDetailsActivity;

public class TermActivity extends MainActivity {

    TreeMap<String, Term> TermList = new TreeMap<>();
    ArrayList<String> items = new ArrayList<>();
    ArrayAdapter<String> adapter;

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
                startActivity(new Intent(TermActivity.this, TermDetailsActivity.class));
            }
        });

       LoadTermList();
        // drawer.setLayoutParams(null);

    }

    private void LoadTermList(){
        //load the term list
        Cursor c = null;

        try {
            Term termItem = null;
            //get the list
            ListView termListAdpt = findViewById(R.id.termListView);
            //set the adapter for list

            //get the database access
            database = new dbSqlLiteManager(this).getReadableDatabase(); //get the database access
            c = database.rawQuery("SELECT title,start_date,end_date,term_starts,term_ends,notes,assesssment_id,mentor_id FROM term ", null);
            adapter = new ArrayAdapter<String>(TermActivity.this, android.R.layout.simple_list_item_1, items);
            if (c.moveToFirst()) {
                do {
                    termItem = new Term(c.getString(3), c.getString(4), c.getString(0), c.getString(1),
                            c.getString(2), c.getString(5), c.getString(6), c.getString(7));
                    items.add(c.getString(0));// Add items to the adapter
                    TermList.put(c.getString(0),termItem);//add the list of items to the treemap for later retrival
                   } while (c.moveToNext());

            }
            termListAdpt. setAdapter(adapter);
            database.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}