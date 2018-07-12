package com.ville.devproc.projecttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.ui.Projects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchViewProjects(View view) {
        Intent intent = new Intent(this, Projects.class);
        startActivity(intent);
    }

}
