package com.example.wguplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;

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
                Snackbar.make(view, "Replace to add new term", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // drawer.setLayoutParams(null);

    }


}