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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.TreeMap;

import Database.dbSqlLiteManager;
import Database.dbTerm;
import Utilities.TermData;
import models.Term;
import wguplanner_details.CourseDetailsActivity;
import wguplanner_details.TermDetailsActivity;

public class TermActivity extends MainActivity {


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
                Intent intent = new Intent(TermActivity.this, TermDetailsActivity.class);
                startActivity(intent);
            }
        });

        //remove courses from the assignedList
        final ListView terms = findViewById(R.id.termListView);
        terms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //Load the term into the TermDetailsActivity
                    String termName = terms.getItemAtPosition(position).toString();
                    Intent intent = new Intent(TermActivity.this, TermDetailsActivity.class);
                    intent.putExtra("Term", termName);
                    startActivity(intent);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

       LoadTermList();
        // drawer.setLayoutParams(null);

    }

    private void LoadTermList(){
        //load the term list
        Cursor c = null;
        TreeMap<String, Term> data = new TreeMap<>();
        try {
            Term termItem = null;
            //get the list
            ListView termListAdpt = findViewById(R.id.termListView);
            //set the adapter for list

            //get the database access
            database = new dbSqlLiteManager(this).getReadableDatabase(); //get the database access
            c = database.rawQuery("SELECT title,start_date,end_date,notes FROM term ", null);
            adapter = new ArrayAdapter<String>(TermActivity.this, android.R.layout.simple_list_item_1, items);
            if (c.moveToFirst()) {
                do {
                    termItem = new Term(c.getString(0), c.getString(1), c.getString(2), c.getString(3));
                    items.add(c.getString(0));// Add items to the adapter
                    data.put(c.getString(0),termItem);//add the list of items to the treemap for later retrival
                   } while (c.moveToNext());

            }
            TermData.setTermList(data);
            termListAdpt. setAdapter(adapter);
            database.close();
        }catch(Exception e){
            if (e.getMessage().contains("no such table")){
                database = new dbSqlLiteManager(this).getWritableDatabase();
                database.execSQL(dbTerm.CREATE_TABLE);
            }
            e.printStackTrace();
        }
    }


}