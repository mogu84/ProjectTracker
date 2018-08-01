package com.ville.devproc.projecttracker.ui.Project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;

public class Projects extends AppCompatActivity {

    private DBHelper mDbHelper;

    private RecyclerView mRecyclerView;
    private ProjectListAdapter mAdapter;
    private CheckBox mSelectCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mSelectCheckBox = findViewById(R.id.projectSelectAll);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        // bind select all functionality to listener and handle required functions
        mSelectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAdapter.checkAllItems(isChecked);
            }
        });

    }

    public void deleteSelectedProjects(View view) {
        mAdapter.deleteCheckedProjects();
    }

    public void launchAddProject(View view) {
        Intent intent = new Intent(this, AddOrUpdateProject.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAdapter.notifyDataSetChanged();
    }
}
