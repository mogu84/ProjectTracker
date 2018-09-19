package com.ville.devproc.projecttracker.ui.Timesheet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;

public class AddTimesheetListProjectsAdapter extends RecyclerView.Adapter<AddTimesheetListProjectsAdapter.ProjectViewHolder> {

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


    public AddTimesheetListProjectsAdapter(Context context, DBHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
        rootView = ((Activity)mContext).findViewById(android.R.id.content);
    }

    @Override
    public AddTimesheetListProjectsAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.timesheetprojectlist_item, parent, false);
        return new ProjectViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddTimesheetListProjectsAdapter.ProjectViewHolder holder, final int position) {

        final Project current = mDB.queryTimesheetProject(position);

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

                Intent intent = new Intent(mContext, AddProjectTimesheet.class);
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