package com.ville.devproc.projecttracker.ui.ProjectWorker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Worker;
import com.ville.devproc.projecttracker.ui.Worker.AddOrUpdateWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectWorkerListAdapter extends RecyclerView.Adapter<ProjectWorkerListAdapter.WorkerViewHolder> {

    /**
     *  Custom view holder with a text view and a check box.
     */
    class WorkerViewHolder extends RecyclerView.ViewHolder {
        public final TextView workerItemView;
        CheckBox workerCheckBoxView;

        public WorkerViewHolder(View itemView) {
            super(itemView);
            workerItemView = itemView.findViewById(R.id.worker);
            workerCheckBoxView = itemView.findViewById(R.id.workerCheckBox);
        }

        public void bind(int position) {
            // use the sparse boolean array to check
            if (!itemStateArray.get(position, false)) {
                workerCheckBoxView.setChecked(false);
            } else {
                workerCheckBoxView.setChecked(true);
            }
        }
    }

    private LayoutInflater mInflater;
    Context mContext;
    DBHelper mDB;
    private HashMap<Integer, Worker> mCheckToWorkerMap = new HashMap<>();
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();
    private View rootView;
    private Button deleteBtn;
    private int projectId;

    public ProjectWorkerListAdapter(Context context, DBHelper db, int projectId) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
        rootView = ((Activity)mContext).findViewById(android.R.id.content);
        deleteBtn = rootView.findViewById(R.id.projectWorkerDeleteButton);
        this.projectId = projectId;
    }

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.workerlist_item, parent, false);
        return new ProjectWorkerListAdapter.WorkerViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerViewHolder holder, int position) {
        final Worker current = mDB.queryProjectWorkers(position, projectId);
        final int mAdapterPos = holder.getAdapterPosition();

        holder.workerItemView.setText(current.getName());
        holder.bind(mAdapterPos);

        // Keep a reference to the view holder for the click listener
        final WorkerViewHolder h = holder; // needs to be final for use in callback

        // Attach a click listener to the CheckBox'es for the DELETE operation.
        holder.workerCheckBoxView.setOnClickListener(new WorkerOnClickListener( current ) {
            @Override
            public void onClick(View v) {
                if (!itemStateArray.get(mAdapterPos, false)) {
                    ((CheckBox)v).setChecked(true);
                    itemStateArray.put(mAdapterPos, true);
                    mCheckToWorkerMap.put(mWorker.getId(), mWorker);

                    deleteBtn.setEnabled(true);
                } else {
                    ((CheckBox)v).setChecked(false);
                    itemStateArray.put(mAdapterPos, false);
                    mCheckToWorkerMap.remove(mWorker.getId());

                    if( mCheckToWorkerMap.size() <= 0 )
                        deleteBtn.setEnabled(false);
                }
            }
        });

        // Attach a click listener to the text views for the UPDATE activity
        holder.workerItemView.setOnClickListener(new WorkerOnClickListener( current ) {
            @Override
            public void onClick(View v) {
                // TODO: add an UPDATE activity here
                /*
                Snackbar.make(v, "View position #" + mPosition + ", adapter pos #" + mAdapterPos + ".", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */

                Intent intent = new Intent(mContext, AddOrUpdateWorker.class);
                intent.putExtra(Worker.TABLE_NAME, current.toString());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (int)mDB.getProjectWorkersCount(projectId);
    }

    protected class WorkerOnClickListener implements View.OnClickListener {

        Worker mWorker;

        public WorkerOnClickListener(Worker worker) {
            this.mWorker = worker;
        }

        @Override
        public void onClick(View v) {
            // implemented in WorkerListAdapter
        }
    }

    public void deleteCheckedWorkers() {

        Boolean isDeleteSuccess = false;
        if(mCheckToWorkerMap.keySet().size() > 0)
            isDeleteSuccess = mDB.deleteProjectWorkers( projectId, new ArrayList<>( mCheckToWorkerMap.keySet() ) );

        if( isDeleteSuccess ) {
            checkAllItems(false);
            Log.v(this.getClass().getName(), "deleteProjectWorker Success.");
        } else {
            Log.v(this.getClass().getName(), "deleteProjectWorker Failure.");
            Snackbar.make(((Activity) mContext).findViewById(R.id.projectWorkerDeleteButton), "Deleting items was unsuccessful.",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void checkAllItems( Boolean selectAll ) {
        if( selectAll ) {
            for (int i = 0; i < this.getItemCount(); i++) {
                itemStateArray.put(i, true);
            }

            List<Worker> workers = mDB.getProjectWorkers(projectId);
            for (Worker worker : workers) {
                mCheckToWorkerMap.put(worker.getId(), worker);
            }

            deleteBtn.setEnabled(true);

        } else {
            itemStateArray.clear();
            mCheckToWorkerMap.clear();
            deleteBtn.setEnabled(false);
        }

        this.notifyDataSetChanged();
    }

}
