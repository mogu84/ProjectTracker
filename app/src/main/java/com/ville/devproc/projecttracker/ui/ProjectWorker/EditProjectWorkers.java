package com.ville.devproc.projecttracker.ui.ProjectWorker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;

public class EditProjectWorkers extends AppCompatActivity {
    private Project selectedProject;
    private TextView projectName;
    private TextView projectDescription;
    private TextView projectLocation;
    private TextView projectStartDate;
    private TextView projectEndDate;

    private DBHelper mDbHelper;

    private RecyclerView mRecyclerView;
    private ProjectWorkerListAdapter mAdapter;
    private CheckBox mSelectCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project_workers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        projectName = findViewById(R.id.projectWorkerNameTextView);     // Get a handle to project name TextView
        projectDescription = findViewById(R.id.projectWorkerDescriptionTextView);
        projectLocation = findViewById(R.id.projectWorkerLocationTextView);
        projectStartDate = findViewById(R.id.projectWorkerStartDateTextView);
        projectEndDate = findViewById(R.id.projectWorkerEndDateTextView);
        mSelectCheckBox = findViewById(R.id.projectWorkerSelectAll);    // Get a handle to selectAll CheckBox
        mRecyclerView = findViewById(R.id.projectWorkerRecyclerView);   // Get a handle to RecyclerView.

        Intent intent = getIntent();
        if( intent.hasExtra(Project.TABLE_NAME) ) {
            selectedProject = new Project(intent.getStringExtra( Project.TABLE_NAME) );
            projectName.setText( selectedProject.getName() );
            projectDescription.setText( selectedProject.getDescription() );
            projectLocation.setText( selectedProject.getLocation() );
            projectStartDate.setText( String.valueOf( selectedProject.getStartDate() ) );   // TODO: convert long to date
            projectEndDate.setText( String.valueOf( selectedProject.getEndDate() ) );   // TODO: convert long to date
        }

        mDbHelper = new DBHelper(getApplicationContext());

        // Create an adapter and supply the data to be displayed.
        mAdapter = new ProjectWorkerListAdapter(this, mDbHelper, selectedProject.getId());
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

    public void deleteSelectedWorkers(View view) {
        mAdapter.deleteCheckedWorkers();
    }

}
