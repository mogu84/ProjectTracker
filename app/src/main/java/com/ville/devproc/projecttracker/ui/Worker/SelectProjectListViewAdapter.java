package com.ville.devproc.projecttracker.ui.Worker;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;
import com.ville.devproc.projecttracker.data.db.model.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectProjectListViewAdapter extends BaseAdapter {

    // Declare Variables
    private Context mContext;
    private LayoutInflater inflater;
    private DBHelper mDbHelper;
    private Bundle mArgs;
    private List<Project> sortList = null;
    private ArrayList<Project> arraylist;

    public SelectProjectListViewAdapter(Context context, List<Project> fullList, DBHelper dbhelper, Bundle arguments) {
        mContext = context;
        this.sortList = fullList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(fullList);
        this.mDbHelper = dbhelper;
        this.mArgs = arguments;
    }

    @Override
    public int getCount() {
        return sortList.size();
    }

    @Override
    public Object getItem(int position) {
        return sortList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final Project current = sortList.get(position);

        if (view == null)
            view = inflater.inflate(R.layout.dialog_projectname_item, null);

        TextView nameText = view.findViewById(R.id.projectName);
        nameText.setText(current.getName());

        final View finalView = view;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if current Worker already assigned to selected project
                Boolean projectWorkerExists = mDbHelper.checkProjectWorker( current.getId(), mArgs.getInt(Worker.COLUMN_WORKER_ID));
                if( !projectWorkerExists ) {
                    mDbHelper.createProjectWorker(current.getId(), mArgs.getInt(Worker.COLUMN_WORKER_ID));
                    sortList.remove(position);
                    notifyDataSetChanged();
                } else {
                    Snackbar.make(finalView, "Worker already assigned to selected project.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        sortList.clear();
        if (charText.length() == 0) {
            sortList.addAll(arraylist);
        }
        else
        {
            for (Project project : arraylist)
            {
                if (project.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    sortList.add(project);
                }
            }
        }
        notifyDataSetChanged();
    }

}
