package com.ville.devproc.projecttracker.ui.Worker;

import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Worker;

public class AddOrUpdateWorker extends AppCompatActivity implements DialogInterface.OnDismissListener {

    private static String LOG_TEXT = "AddOrUpdateProject";

    private Boolean isUpdateWorker;
    private Worker selectedWorker;
    private EditText mWorkerName;
    private DBHelper mDbHelper;

    private ConstraintLayout mWorkerProjects;
    private RecyclerView mRecyclerView;
    private WorkerProjectListAdapter mAdapter;
    private CheckBox mSelectCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDbHelper = new DBHelper(getApplicationContext());

        mWorkerName = findViewById(R.id.wName_view);
        mWorkerProjects = findViewById(R.id.workerProjectsList);

        Intent intent = getIntent();        // Intent to get EXTRA data for UPDATE functionality
        isUpdateWorker = intent.hasExtra(Worker.TABLE_NAME);
        Button mAssignWorker = findViewById(R.id.wAssignProject);

        // In update set data from existing values
        if( isUpdateWorker ) {
            mAssignWorker.setVisibility(View.VISIBLE);
            mWorkerProjects.setVisibility(View.VISIBLE);

            getSupportActionBar().setTitle(getString(R.string.title_activity_update_worker));

            selectedWorker = new Worker(intent.getStringExtra(Worker.TABLE_NAME));

            mWorkerName.setText(selectedWorker.getName());


            // Setup Projects already assigned to selected worker
            mRecyclerView = findViewById(R.id.workerProjectListView);
            // Create an adapter and supply the data to be displayed.
            mAdapter = new WorkerProjectListAdapter(this, mDbHelper, selectedWorker.getId());
            // Connect the adapter with the RecyclerView.
            mRecyclerView.setAdapter(mAdapter);
            // Give the RecyclerView a default layout manager.
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        } else {
            mAssignWorker.setVisibility(View.INVISIBLE);
            mWorkerProjects.setVisibility(View.INVISIBLE);
            selectedWorker = new Worker();
        }
    }

    /**
     * Adds the inputted project into Database
     */
    public void submitWorker(View view) {

        Boolean isSubmitAllowed = false;
        Button btn = findViewById(R.id.wAddButton);

        String workerName = mWorkerName.getText().toString();
        if (!workerName.isEmpty() && !workerName.trim().isEmpty()) {
            selectedWorker.setName(workerName);
            isSubmitAllowed = true;
        } else {
            Snackbar.make(view, "Worker name can't be empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        Log.d(LOG_TEXT, "ADD BUTTON PUSHED: ");

        if (isSubmitAllowed) {
            if( isUpdateWorker )
                mDbHelper.updateWorker(selectedWorker);
            else
                mDbHelper.createWorker(selectedWorker);

            // Return back to Workers view and reset activity back stack
            Intent workersIntent = new Intent(this, Workers.class);

            PendingIntent pendingIntent =
                    TaskStackBuilder.create(this)
                            // add all of Worker's parents to the stack,
                            // followed by the Worker itself.
                            .addNextIntentWithParentStack(workersIntent)
                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            // TODO: add popup query here, whether the user wants to also update the created worker (like adding workers).

            try {
                pendingIntent.send(this, 0, workersIntent);
            } catch (Exception e) {
                Log.d(LOG_TEXT, "VIRHE: " + e.getMessage());
            }
        }
    }

    public void assignToProject(View view) {
        DialogFragment dialog = new SelectProjectDialogFragment();
        Bundle args = new Bundle();
        args.putInt(Worker.COLUMN_WORKER_ID, selectedWorker.getId());
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "abc");
    }

    public void deleteSelectedProjects(View view) {
        mAdapter.deleteCheckedProjects();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // Fragment dialog had been dismissed
        Log.v(this.getClass().getName(), "DialogFragment Dismissed");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAdapter.notifyDataSetChanged();
    }
}
