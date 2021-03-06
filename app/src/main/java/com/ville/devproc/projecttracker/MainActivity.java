package com.ville.devproc.projecttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.ui.Project.Projects;
import com.ville.devproc.projecttracker.ui.Timesheet.AddTimesheetListProjects;
import com.ville.devproc.projecttracker.ui.Timesheet.ViewTimesheetListProjects;
import com.ville.devproc.projecttracker.ui.Worker.Workers;

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

    public void launchViewWorkers(View view) {
        Intent intent = new Intent(this, Workers.class);
        startActivity(intent);
    }

    public void launchAddTimesheets(View view) {
        Intent intent = new Intent(this, AddTimesheetListProjects.class);
        startActivity(intent);
    }

    public void launchViewTimesheets(View view) {
        Intent intent = new Intent(this, ViewTimesheetListProjects.class);
        startActivity(intent);
    }

    public void launchTestActivity(View view){
        /*Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);*/
    }

}
