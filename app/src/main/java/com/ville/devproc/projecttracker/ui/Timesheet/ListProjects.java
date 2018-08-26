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

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.ui.Project.ProjectListAdapter;

public class ListProjects extends AppCompatActivity {

    private DBHelper mDbHelper;
    private RecyclerView mRecyclerView;
    private TimesheetProjectListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_projects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDbHelper = new DBHelper(getApplicationContext());

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.lstProjectRecyclerView);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new TimesheetProjectListAdapter(this, mDbHelper);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            default:
                break;
            case -1:
                Snackbar.make(mRecyclerView, "Can't add timesheets to an empty project.", Snackbar.LENGTH_LONG)
                      .setAction("Action", null).show();
                break;
        }
    }
}
