package com.ville.devproc.projecttracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;

import java.util.LinkedList;
import java.util.List;

public class Projects extends AppCompatActivity {

    private DBHelper mDbHelper;

    private RecyclerView mRecyclerView;
    private ProjectListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
    }

    public void launchAddProject(View view) {
        Intent intent = new Intent(this, AddProject.class);
        startActivity(intent);
    }
}
