package com.ville.devproc.projecttracker.ui.Timesheet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
        holder.mWorkerHour.setText( Double.toString( current.getDuration() ) );

        // Keep a reference to the view holder for the click listener
        final ProjectViewHolder h = holder; // needs to be final for use in callback

        // Attach a click listener to the text views for the UPDATE activity
        /*
        holder.workerItemView.setOnClickListener(new ProjectOnClickListener( current ) {
            @Override
            public void onClick(View v) {

                //Snackbar.make(v, "View adapter pos #" + mAdapterPos + ".", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return (int)mDB.getTimesheetProjectCount();
    }


    protected class ProjectOnClickListener implements View.OnClickListener {

        Project mProject;

        public ProjectOnClickListener(Project project) {
            this.mProject = project;
        }

        @Override
        public void onClick(View v) {
            // implemented in ProjectListAdapter
        }
    }


}