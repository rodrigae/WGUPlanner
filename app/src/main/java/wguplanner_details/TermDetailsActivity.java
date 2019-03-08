package wguplanner_details;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;

import com.example.wguplanner.R;
import com.example.wguplanner.MainActivity;
public class TermDetailsActivity extends MainActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_course_detail);

        fab = (FloatingActionButton) findViewById(R.id.saveCourse);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace to save new course details", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // drawer.setLayoutParams(null);

    }


}