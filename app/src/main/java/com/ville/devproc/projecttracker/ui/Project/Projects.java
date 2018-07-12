package com.ville.devproc.projecttracker.ui.Project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;

public class Projects extends AppCompatActivity {

    private DBHelper mDbHelper;

    private RecyclerView mRecyclerView;
    private ProjectListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */
                launchAddProject(view);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDbHelper = new DBHelper(getApplicationContext());

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.projectRecyclerView);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new ProjectListAdapter(this, mDbHelper);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO 1: add DELETE selected projects functionality here (bind to delete button onClickListener).
        // TODO 2: add select or deselect all project checkboxes functionality here (bind to the check all).
    }

    public void launchAddProject(View view) {
        Intent intent = new Intent(this, AddProject.class);
        startActivity(intent);
    }
}
