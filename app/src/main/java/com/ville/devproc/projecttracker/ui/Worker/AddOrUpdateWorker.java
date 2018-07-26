package com.ville.devproc.projecttracker.ui.Worker;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;
import com.ville.devproc.projecttracker.data.db.model.Worker;

public class AddOrUpdateWorker extends AppCompatActivity {

    private static String LOG_TEXT = "AddOrUpdateProject";

    private Boolean isUpdateWorker;
    private Worker selectedWorker;
    private EditText mWorkerName;
    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWorkerName = findViewById(R.id.wName_view);


        mDbHelper = new DBHelper(getApplicationContext());

        Intent intent = getIntent();        // Intent to get EXTRA data for UPDATE functionality
        isUpdateWorker = intent.hasExtra(Worker.COLUMN_NAME);
        selectedWorker = new Worker();

        // In update set data from existing values
        if( isUpdateWorker ) {
            getSupportActionBar().setTitle(getString(R.string.title_activity_update_worker));

            selectedWorker.setId(intent.getIntExtra(Worker.COLUMN_WORKER_ID, -1));
            selectedWorker.setName(intent.getStringExtra(Worker.COLUMN_NAME));

            mWorkerName.setText(selectedWorker.getName());
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

}
