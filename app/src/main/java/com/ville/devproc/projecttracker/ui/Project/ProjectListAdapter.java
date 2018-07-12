package com.ville.devproc.projecttracker.ui.Project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder> {

    /**
     *  Custom view holder with a text view and a check box.
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
                projectCheckBoxView.setChecked(false);}
            else {
                projectCheckBoxView.setChecked(true);
            }
        }
    }

    private LayoutInflater mInflater;
    DBHelper mDB;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    Context mContext;

    public ProjectListAdapter(Context context, DBHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
    }

    @Override
    public ProjectListAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.projectlist_item, parent, false);
        return new ProjectViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectListAdapter.ProjectViewHolder holder, int position) {
        Project current = mDB.query(position);
        final int mAdapterPos = holder.getAdapterPosition();
        holder.projectItemView.setText(current.getName());
        final int mPosition = holder.getLayoutPosition();

        holder.bind(position);

        // Keep a reference to the view holder for the click listener
        final ProjectViewHolder h = holder; // needs to be final for use in callback

        // Attach a click listener to the CheckBox'es for the DELETE operation.
        holder.projectCheckBoxView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemStateArray.get(mAdapterPos, false)) {
                    ((CheckBox)v).setChecked(true);
                    itemStateArray.put(mAdapterPos, true);
                }
                else  {
                    ((CheckBox)v).setChecked(false);
                    itemStateArray.put(mAdapterPos, false);
                }
            }
        });

        // Attach a click listener to the text views for the UPDATE activity
        holder.projectItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add an UPDATE activity here
                Snackbar.make(v, "View position #" + mPosition + ", adapter pos #" + mAdapterPos + ".", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDB.getProjectCount();
    }
}
