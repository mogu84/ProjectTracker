package com.ville.devproc.projecttracker.ui;

import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.MainActivity;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddProject extends AppCompatActivity {

    private TextView mStartPickDate;
    private TextView mEndPickDate;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDbHelper = new DBHelper(getApplicationContext());

        /* Capture View elements for the start date function */
        mStartPickDate = (TextView) findViewById(R.id.pStartdate_view);
        /* get the current date */
        mStartDate = Calendar.getInstance();
        /* display the current date (this method is below)  */
        updateDisplay(mStartPickDate, mStartDate);

        /* add a click listener to the TextView */
        mStartPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("DATE",R.id.pStartdate_view);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);

                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        /* Capture View elements for the end date function */
        mEndPickDate = (TextView) findViewById(R.id.pEnddate_view);
        /* get the current date */
        mEndDate = Calendar.getInstance();
        /* display the current date (this method is below)  */
        updateDisplay(mEndPickDate, mEndDate);

        /* add a click listener to the TextView */
        mEndPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("DATE",R.id.pEnddate_view);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);

                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }


    /** Adds the inputted project into Database */
    public void submitProject(View view) {

        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("fi", "FI"));
        Button btn = findViewById(R.id.pAddButton);

        Project project = new Project();
        // TODO: check if name is empty
        String projectName = ((EditText) findViewById(R.id.pName_view)).getText().toString();
        if( projectName != null && !projectName.isEmpty() && !projectName.trim().isEmpty() ) {
            project.setName(projectName);
            btn.setEnabled(true);
        } else {
            Snackbar.make(view, "Project name can't be empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            btn.setEnabled(false);
        }
        project.setDescription( ((EditText) findViewById(R.id.pName_view)).getText().toString().trim() );
        project.setLocation( ((EditText) findViewById(R.id.pName_view)).getText().toString().trim() );

        // Parse and set project start date
        String mStartDateText = ((TextView) findViewById(R.id.pStartdate_view)).getText().toString().trim();
        try {
            Date mStartDateDate = sdf.parse(mStartDateText);
            project.setStartDate( mStartDateDate.getTime() );
        } catch (Exception e) {
            Snackbar.make(view, "Start Date parse failure", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        // Parse and set project end date
        String mEndDateText = ((TextView) findViewById(R.id.pEnddate_view)).getText().toString().trim();
        try {
            Date mEndDateDate = sdf.parse( mEndDateText );
            project.setEndDate(mEndDateDate.getTime());
        } catch (Exception e) {
            Snackbar.make(view, "End Date parse failure", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        project.setInputDate( Calendar.getInstance().getTimeInMillis() );

        Snackbar.make(view, "Project: " + project.toString(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        mDbHelper.createProject(project);


        // Return back to Projects view and reset activity back stack
        Intent projectsIntent = new Intent(this, Projects.class);

        PendingIntent pendingIntent =
                TaskStackBuilder.create(this)
                    // add all of Project's parents to the stack,
                    // followed by the Project itself.
                    .addNextIntentWithParentStack(projectsIntent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        try {
            pendingIntent.send(this, 0, projectsIntent);
        } catch(Exception e ) {
            Log.d("AddProjects", "VIRHE: " + e.getMessage());
        }
    }

    private void updateDisplay(TextView editField, Calendar date) {
        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("fi", "FI"));

        editField.setText(sdf.format(date.getTime()));
    }
}