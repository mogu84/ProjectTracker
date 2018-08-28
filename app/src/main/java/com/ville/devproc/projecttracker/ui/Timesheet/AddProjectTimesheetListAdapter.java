package com.ville.devproc.projecttracker.ui.Timesheet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;
import com.ville.devproc.projecttracker.data.db.model.Timesheet;
import com.ville.devproc.projecttracker.data.db.model.Worker;

import java.util.HashMap;

public class AddProjectTimesheetListAdapter extends RecyclerView.Adapter<AddProjectTimesheetListAdapter.ProjectViewHolder> {

    /**
     *  Custom view holder with a textview and a checkbox.
     */
    class ProjectViewHolder extends RecyclerView.ViewHolder {
        public final TextView mWorkerName;
        public final EditText mWorkerHour;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            mWorkerName = itemView.findViewById(R.id.add_timesheet_workername);
            mWorkerHour = itemView.findViewById(R.id.add_timesheet_workerhours);
        }

        public void bind(int position) {
            // use the sparse boolean array to check
        }
    }

    private LayoutInflater mInflater;
    private Context mContext;
    private DBHelper mDB;
    private View rootView;
    private int mProjectId;
    private long mDate;
    private HashMap<Integer, Timesheet> mTimesheetMap = new HashMap<>();


    public AddProjectTimesheetListAdapter(Context context, DBHelper db, int projectId, long date) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
        rootView = ((Activity)mContext).findViewById(android.R.id.content);
        this.mProjectId = projectId;
        this.mDate = date;
    }

    @Override
    public AddProjectTimesheetListAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.timesheetworkerhours_item, parent, false);
        return new ProjectViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddProjectTimesheetListAdapter.ProjectViewHolder holder, final int position) {

        final Timesheet current = mDB.queryTimesheetProject(mProjectId, mDate, position);
        //final int mAdapterPos = holder.getAdapterPosition();
        //holder.bind(mAdapterPos);

        holder.mWorkerName.setText(current.getWorkerName());
        holder.mWorkerHour.setText( Double.toString( current.getDuration()/60 ) );

        // Keep a reference to the view holder for the click listener
        final ProjectViewHolder h = holder; // needs to be final for use in callback

        // Attach a click listener to the text views for the UPDATE activity
        holder.mWorkerHour.setOnFocusChangeListener(new HourOnChangeListener( current ) {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if( !hasFocus ) {
                    double setValue = Double.parseDouble(h.mWorkerHour.getText().toString());
                    current.setDuration((long) (setValue * 60));
                    mTimesheetMap.put(current.getWorkerId(), current);
                    Log.v("WorkerHOUR", current.getWorkerName() + ": " + current.toString());
                }

                //Snackbar.make(v, "View adapter pos #" + mAdapterPos + ".", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (int)mDB.getTimesheetProjectWorkerCount(mProjectId, mDate);
    }


    protected void updateDate(long date) { this.mDate = date; }

    protected class HourOnChangeListener implements View.OnFocusChangeListener {

        Timesheet mTimesheet;

        public HourOnChangeListener(Timesheet timesheet) {
            this.mTimesheet = timesheet;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            // implemented in ListAdapter
        }
    }


    protected boolean saveTimesheetHours() {

        try {
            for (Timesheet sheet : mTimesheetMap.values()) {
                if (sheet.getTimesheetId() != 0) {
                    mDB.updateTimesheet(sheet);     // ALWAYS UPDATE EXSISTING TIMESHEET
                } else {
                    if (sheet.getDuration() > 0)
                        mDB.createTimesheet(sheet); // CREATE ONLY TIMESHEETS WITH DURATION
                }
            }
        } catch( Exception e) {
            Log.e("AddTimesheet", "Error updating timesheet: " + e.getMessage());
            return false;
        }

        return true;
    }

}