package com.ville.devproc.projecttracker.ui.Project;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder> {

    /**
     *  Custom view holder with a textview and a checkbox.
     */
    class ProjectViewHolder extends RecyclerView.ViewHolder {
        public final TextView projectItemView;
        CheckBox projectCheckBoxView;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            projectItemView = itemView.findViewById(R.id.project);
            projectCheckBoxView = itemView.findViewById(R.id.projectCheckBox);
        }

        public void bind(int position) {
            // use the sparse boolean array to check
            if (!itemStateArray.get(position, false)) {
                projectCheckBoxView.setChecked(false);
            } else {
                projectCheckBoxView.setChecked(true);
            }
        }
    }

    private LayoutInflater mInflater;
    Context mContext;
    DBHelper mDB;
    private HashMap<Integer, Project> mCheckToProjectMap = new HashMap<>();
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();
    private View rootView;
    private Button deleteBtn;


    public ProjectListAdapter(Context context, DBHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
        rootView = ((Activity)mContext).findViewById(android.R.id.content);
        deleteBtn = rootView.findViewById(R.id.projectDeleteButton);
    }

    @Override
    public ProjectListAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.projectlist_item, parent, false);
        return new ProjectViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectListAdapter.ProjectViewHolder holder, final int position) {

        final Project current = mDB.query(position);
        final int mAdapterPos = holder.getAdapterPosition();
        final int mPosition = holder.getLayoutPosition();

        holder.projectItemView.setText(current.getName());
        holder.bind(mAdapterPos);

        // Keep a reference to the view holder for the click listener
        final ProjectViewHolder h = holder; // needs to be final for use in callback

        // Attach a click listener to the CheckBox'es for the DELETE operation.
        holder.projectCheckBoxView.setOnClickListener(new ProjectOnClickListener( current ) {
            @Override
            public void onClick(View v) {
                if (!itemStateArray.get(mAdapterPos, false)) {
                    ((CheckBox)v).setChecked(true);
                    itemStateArray.put(mAdapterPos, true);
                    mCheckToProjectMap.put(mProject.getId(), mProject);

                    deleteBtn.setEnabled(true);
                } else {
                    ((CheckBox)v).setChecked(false);
                    itemStateArray.put(mAdapterPos, false);
                    mCheckToProjectMap.remove(mProject.getId());

                    if( mCheckToProjectMap.size() <= 0 )
                        deleteBtn.setEnabled(false);
                }
            }
        });

        // Attach a click listener to the text views for the UPDATE activity
        holder.projectItemView.setOnClickListener(new ProjectOnClickListener( current ) {
            @Override
            public void onClick(View v) {
                // TODO: add an UPDATE activity here
                /*
                Snackbar.make(v, "View position #" + mPosition + ", adapter pos #" + mAdapterPos + ".", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */

                Intent intent = new Intent(mContext, AddOrUpdateProject.class);
                intent.putExtra(Project.TABLE_NAME, current.toString());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (int)mDB.getProjectCount();
    }

    public void deleteCheckedProjects() {

        Boolean isDeleteSuccess = false;
        if(mCheckToProjectMap.keySet().size() > 0)

            // Deletes only the selected Projects from the ProjectWorker table.
            mDB.deleteAllProjectWorker( new ArrayList<>(mCheckToProjectMap.keySet()));

            isDeleteSuccess = mDB.deleteProjects( new ArrayList<>( mCheckToProjectMap.keySet() ) );

        if( isDeleteSuccess ) {
            checkAllItems(false);
        } else {
            Snackbar.make(((Activity) mContext).findViewById(R.id.projectDeleteButton), "Deleting items was unsuccessful.",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
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

    public void checkAllItems( Boolean selectAll ) {
        if( selectAll ) {
            for (int i = 0; i < this.getItemCount(); i++) {
                itemStateArray.put(i, true);
            }

            List<Project> projects = mDB.getAllProjects();
            for (Project project : projects) {
                mCheckToProjectMap.put(project.getId(), project);
            }

            deleteBtn.setEnabled(true);

        } else {
            itemStateArray.clear();
            mCheckToProjectMap.clear();
            deleteBtn.setEnabled(false);
        }

        this.notifyDataSetChanged();
    }

}