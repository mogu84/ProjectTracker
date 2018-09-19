package com.ville.devproc.projecttracker.ui.Timesheet;

import android.app.DialogFragment;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;
import com.ville.devproc.projecttracker.ui.Project.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AddProjectTimesheet extends AppCompatActivity implements AddTimesheetDatePickerFragment.DatePickerDialogListener {
    private Project mSelectedProject;
    private TextView mProjectNameTextView;
    private TextView mProjectTimesheetDate;
    private RecyclerView mRecyclerView;
    private DBHelper mDbHelper;
    private Calendar mCalendar;
    private AddProjectTimesheetListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timesheet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch(Exception e) {
            Log.e("AddProjectTimesheet", "setDisplayHomeAsUpEnabled error: " + e.getMessage());
        }

        Intent intent = getIntent();
        if(!intent.hasExtra(Project.TABLE_NAME)) {
            // No project, nothing to show
            setResult(-1, intent);
            finish();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTimesheets(v);
            }
        });

        mProjectNameTextView = findViewById(R.id.add_timesheet_projectname);
        mProjectTimesheetDate = findViewById(R.id.add_timesheet_date);
        mProjectTimesheetDate.setOnClickListener(dateListener);
        mRecyclerView = findViewById(R.id.add_timesheet_recyclerView);
        mDbHelper = new DBHelper(getApplicationContext());

        // avoid automatically appear android keyboard when activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // display project name
        mSelectedProject = new Project(intent.getStringExtra(Project.TABLE_NAME));
        mProjectNameTextView.setText(mSelectedProject.getName());

        mCalendar = Calendar.getInstance();
        mCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        mCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Helsinki"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", new Locale("fi", "FI"));
        mProjectTimesheetDate.setText(sdf.format(mCalendar.getTime()));

        mCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        mAdapter = new AddProjectTimesheetListAdapter(this, mDbHelper, mSelectedProject.getId(), mCalendar.getTimeInMillis() );
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    View.OnClickListener dateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogFragment newDateFragment = new AddTimesheetDatePickerFragment();
            Bundle bundle = new Bundle();
            bundle.putLong("date", mCalendar.getTimeInMillis());
            newDateFragment.setArguments(bundle);
            newDateFragment.show(getFragmentManager(), "AddTimesheetDatePicker");
        }
    };

    @Override
    public void onFinishDatePickerDialogListener(String inputText) {
        mCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Helsinki"));
        mCalendar.setTimeInMillis( Long.parseLong(inputText) );

        updateDateAndView();
    }

    public void updateDateAndView() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", new Locale("fi", "FI"));

        mProjectTimesheetDate.setText(sdf.format(mCalendar.getTime()));

        mCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        mAdapter.updateDate(mCalendar.getTimeInMillis());
        mAdapter.notifyDataSetChanged();

        mProjectNameTextView.setFocusable(true);
        mProjectNameTextView.setFocusableInTouchMode(true);
        mProjectNameTextView.requestFocus();
    }

    public void submitTimesheets(View v) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        boolean result = mAdapter.saveTimesheetHours();

        Intent intent = new Intent(this, AddTimesheetListProjects.class);
        startActivity(intent);
    }

    public void getPrevDay(View v) {
        mCalendar.add(Calendar.DATE, -1);

        updateDateAndView();
    }

    public void getNextDay(View v) {
        mCalendar.add(Calendar.DATE, 1);

        updateDateAndView();
    }

}
