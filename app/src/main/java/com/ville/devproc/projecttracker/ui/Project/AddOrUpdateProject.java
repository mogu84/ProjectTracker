package com.ville.devproc.projecttracker.ui.Project;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;
import com.ville.devproc.projecttracker.ui.ProjectWorker.EditProjectWorkers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddOrUpdateProject extends AppCompatActivity {

    private static String LOG_TEXT = "AddOrUpdateProject";

    private Boolean isUpdateProject;
    private Project selectedProject;
    private EditText mProjectName;
    private EditText mProjectDescription;
    private EditText mProjectLocation;
    private TextView mStartPickDate;
    private TextView mEndPickDate;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private DBHelper mDbHelper;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        mDbHelper = new DBHelper(getApplicationContext());

        Intent intent = getIntent();        // Intent to get EXTRA data for UPDATE functionality
        isUpdateProject = intent.hasExtra(Project.TABLE_NAME);


        // In update set data from existing values
        if( isUpdateProject ) {
            // avoid automatically appear android keyboard when activity start
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            getSupportActionBar().setTitle(getString(R.string.title_activity_update_project));
            mProjectName = findViewById(R.id.pName_view);
            mProjectDescription = findViewById(R.id.pDescription_view);
            mProjectLocation = findViewById(R.id.pLocation_view);

            selectedProject = new Project(intent.getStringExtra(Project.TABLE_NAME));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent backIntent = new Intent(mContext, EditProjectWorkers.class);
                    backIntent.putExtra(Project.TABLE_NAME, selectedProject.toString());
                    setResult(1, backIntent);
                    finish();
                }
            });

            mProjectName.setText(selectedProject.getName());
            mProjectDescription.setText(selectedProject.getDescription());
            mProjectLocation.setText(selectedProject.getLocation());
        } else {
            // automatic popping up keyboard on start Activity
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            selectedProject = new Project();
        }

        /* Capture View elements for the start date function */
        mStartPickDate = findViewById(R.id.pStartdate_view);
        /* get the current date */
        mStartDate = Calendar.getInstance();
        /* display the start date (this method is below)  */
        if (isUpdateProject) {    // check if we have an update call
            mStartDate.setTimeInMillis(selectedProject.getStartDate());
        }
        updateDisplay(mStartPickDate, mStartDate);

        /* add a click listener to the TextView */
        mStartPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("DATE", R.id.pStartdate_view);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);

                newFragment.show(getFragmentManager(), "datePicker");
            }
        });


        /* Capture View elements for the end date function */
        mEndPickDate = findViewById(R.id.pEnddate_view);
        /* get the current date */
        mEndDate = Calendar.getInstance();
        /* display the end date (this method is below)  */
        if (isUpdateProject) {    // check if we have an update call
            mEndDate.setTimeInMillis(selectedProject.getEndDate());
        }
        updateDisplay(mEndPickDate, mEndDate);

        /* add a click listener to the TextView */
        mEndPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("DATE", R.id.pEnddate_view);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);

                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }


    /**
     * Adds the inputted project into Database
     */
    public void submitProject(View view) {

        Boolean isSubmitAllowed = false;
        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("fi", "FI"));

        String projectName = ((EditText) findViewById(R.id.pName_view)).getText().toString();
        if (!projectName.isEmpty() && !projectName.trim().isEmpty()) {
            selectedProject.setName(projectName);
            isSubmitAllowed = true;
        } else {
            Snackbar.make(view, "Project name can't be empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        selectedProject.setDescription(((EditText) findViewById(R.id.pDescription_view)).getText().toString().trim());
        selectedProject.setLocation(((EditText) findViewById(R.id.pLocation_view)).getText().toString().trim());

        // Parse and set project start date
        String mStartDateText = ((TextView) findViewById(R.id.pStartdate_view)).getText().toString().trim();
        try {
            Date mStartDateDate = sdf.parse(mStartDateText);
            selectedProject.setStartDate(mStartDateDate.getTime());
        } catch (Exception e) {
            Snackbar.make(view, "Start Date parse failure", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        // Parse and set project end date
        String mEndDateText = ((TextView) findViewById(R.id.pEnddate_view)).getText().toString().trim();
        try {
            Date mEndDateDate = sdf.parse(mEndDateText);
            selectedProject.setEndDate(mEndDateDate.getTime());
        } catch (Exception e) {
            Snackbar.make(view, "End Date parse failure", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        if(!isUpdateProject)
            selectedProject.setInputDate(Calendar.getInstance().getTimeInMillis());

        Log.d(LOG_TEXT, "ADD BUTTON PUSHED: ");

        if (isSubmitAllowed) {
            if( isUpdateProject )
                mDbHelper.updateProject(selectedProject);
            else
                mDbHelper.createProject(selectedProject);

            Intent intent = new Intent(this, EditProjectWorkers.class);
            intent.putExtra(Project.TABLE_NAME, selectedProject.toString());
            setResult(1, intent);
            finish();
            /*// Return back to Projects view and reset activity back stack
            Intent projectsIntent = new Intent(this, Projects.class );;

            PendingIntent pendingIntent =
                    TaskStackBuilder.create(this)
                            // add all of Project's parents to the stack,
                            // followed by the Project itself.
                            .addNextIntentWithParentStack(projectsIntent)
                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            try {
                pendingIntent.send(this, 0, projectsIntent);
            } catch (Exception e) {
                Log.d(LOG_TEXT, "VIRHE: " + e.getMessage());
            }*/
        }
    }

    /**
     * Opens a view where Project Workers can be handled.
     * */
    public void launchProjectWorkers(View view) {
        Intent intent = new Intent(this, EditProjectWorkers.class);
        intent.putExtra(Project.TABLE_NAME, selectedProject.toString());
        startActivity(intent);
    }

    private void updateDisplay(TextView editField, Calendar date) {
        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("fi", "FI"));

        editField.setText(sdf.format(date.getTime()));
    }
}