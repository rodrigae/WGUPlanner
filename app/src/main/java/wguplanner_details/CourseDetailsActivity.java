package wguplanner_details;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.example.wguplanner.CourseActivity;
import com.example.wguplanner.MainActivity;
import com.example.wguplanner.R;


public class CourseDetailsActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_course_detail);

        fab = (FloatingActionButton) findViewById(R.id.saveCourse);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace to add save course detail", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}