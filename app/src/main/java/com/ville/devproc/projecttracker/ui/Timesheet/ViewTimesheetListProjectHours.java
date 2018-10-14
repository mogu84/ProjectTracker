package com.ville.devproc.projecttracker.ui.Timesheet;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;
import com.ville.devproc.projecttracker.data.db.model.Timesheet;

import java.util.List;

public class ViewTimesheetListProjectHours extends AppCompatActivity {

    private DBHelper mDbHelper;
    private Project mSelectedProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_timesheet_list_project_hours);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(!intent.hasExtra(Project.TABLE_NAME)) {
            // No project, nothing to show
            setResult(-1, intent);
            finish();
        }

        mSelectedProject = new Project(intent.getStringExtra(Project.TABLE_NAME));
        mDbHelper = new DBHelper(getApplicationContext());

        List<Timesheet> lstTimesheets = mDbHelper.getProjectTimesheetByMonth(mSelectedProject.getId());

        for( int i = 0; i < lstTimesheets.size(); i++) {
            System.out.println("ViewTimesheetProjectHoursByMonth\t" + lstTimesheets.get(i).toString());
        }

        View rootView = ((Activity)this).getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, "Loaded timesheets: " + Integer.toString(lstTimesheets.size()), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
}
