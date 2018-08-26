package com.ville.devproc.projecttracker.ui.Timesheet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;
import com.ville.devproc.projecttracker.ui.ProjectWorker.EditProjectWorkers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimesheetProjectListAdapter extends RecyclerView.Adapter<TimesheetProjectListAdapter.ProjectViewHolder> {

    /**
     *  Custom view holder with a textview and a checkbox.
     */
    class ProjectViewHolder extends RecyclerView.ViewHolder {
        public final TextView projectItemView;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            projectItemView = itemView.findViewById(R.id.timesheetProjectName);
        }

        public void bind(int position) {
            // use the sparse boolean array to check
        }
    }

    private LayoutInflater mInflater;
    Context mContext;
    DBHelper mDB;
    private View rootView;


    public TimesheetProjectListAdapter(Context context, DBHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
        rootView = ((Activity)mContext).findViewById(android.R.id.content);
    }

    @Override
    public TimesheetProjectListAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.timesheetprojectlist_item, parent, false);
        return new ProjectViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TimesheetProjectListAdapter.ProjectViewHolder holder, final int position) {

        final Project current = mDB.queryTimesheetProject(position);
        //final int mAdapterPos = holder.getAdapterPosition();
        //holder.bind(mAdapterPos);

        holder.projectItemView.setText(current.getName());

        // Keep a reference to the view holder for the click listener
        final ProjectViewHolder h = holder; // needs to be final for use in callback

        // Attach a click listener to the text views for the UPDATE activity
        holder.projectItemView.setOnClickListener(new ProjectOnClickListener( current ) {
            @Override
            public void onClick(View v) {
                /*
                Snackbar.make(v, "View adapter pos #" + mAdapterPos + ".", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */

                Intent intent = new Intent(mContext, AddTimesheet.class);
                intent.putExtra(Project.TABLE_NAME, current.toString());
                mContext.startActivity(intent);

            }
        });
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