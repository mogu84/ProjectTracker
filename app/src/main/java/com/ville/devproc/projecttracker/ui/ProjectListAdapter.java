package com.ville.devproc.projecttracker.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;

import java.util.LinkedList;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private DBHelper mDB;


    public ProjectListAdapter(Context context, DBHelper db) {
        mInflater = LayoutInflater.from(context);
        mDB = db;
    }

    @NonNull
    @Override
    public ProjectListAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.projectlist_item, parent, false);
        return new ProjectViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectListAdapter.ProjectViewHolder holder, int position) {
        Project current = mDB.query(position);
        holder.projectItemView.setText(current.getName());
    }

    @Override
    public int getItemCount() {
        return mDB.getProjectCount();
    }



    class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView projectItemView;
        final ProjectListAdapter mAdapter;

        public ProjectViewHolder(View itemView, ProjectListAdapter adapter) {
            super(itemView);
            projectItemView = (TextView) itemView.findViewById(R.id.project);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mWordList.
//            String element = mProjectList.get(mPosition);
            // Change the word in the mWordList.
//            mProjectList.set(mPosition, "Clicked! " + element);
            // Notify the adapter, that the data has changed so it can
            // update the RecyclerView to display the data.
            mAdapter.notifyDataSetChanged();
        }
    }
}
