package com.ville.devproc.projecttracker.ui.Timesheet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AddTimesheet extends AppCompatActivity {
    private Project selectedProject;
    private TextView projectNameTextView;
    private TextView projectTimesheetDate;
    private RecyclerView mRecyclerView;
    private DBHelper mDbHelper;
    private Calendar calendar;
    private AddProjectTimesheetListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timesheet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(!intent.hasExtra(Project.TABLE_NAME)) {
            // No project, nothing to show
            setResult(-1, intent);
            finish();
        }

        projectNameTextView = findViewById(R.id.add_timesheet_projectname);
        projectTimesheetDate = findViewById(R.id.add_timesheet_date);
        mRecyclerView = findViewById(R.id.add_timesheet_recyclerView);
        mDbHelper = new DBHelper(getApplicationContext());

        // display project name
        selectedProject = new Project(intent.getStringExtra(Project.TABLE_NAME));
        projectNameTextView.setText(selectedProject.getName());

        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Helsinki"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", new Locale("fi", "FI"));
        projectTimesheetDate.setText(sdf.format(calendar.getTime()));

        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        mAdapter = new AddProjectTimesheetListAdapter(this, mDbHelper, selectedProject.getId(), calendar.getTimeInMillis() );
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));




    }

}
