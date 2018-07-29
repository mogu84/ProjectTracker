package com.ville.devproc.projecttracker.ui.Worker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;
import com.ville.devproc.projecttracker.data.db.model.ProjectWorker;
import com.ville.devproc.projecttracker.data.db.model.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectProjectDialogFragment extends DialogFragment {
    private SelectProjectListViewAdapter adapter;
    private DBHelper mDbHelper;
    private Bundle mArgs;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mArgs = getArguments();

        mDbHelper = new DBHelper(getActivity().getApplicationContext());
        List<Project> projects = mDbHelper.getAllUnassignedProjects(mArgs.getInt(Worker.COLUMN_WORKER_ID));

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_projectselect, null);
        final EditText filterText = v.findViewById(R.id.pFilterByName);
        ListView projectList = v.findViewById(R.id.dSelectProjectListView);

        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(filterText.getText().toString());
            }
        });

        adapter = new SelectProjectListViewAdapter( getActivity().getApplicationContext(), projects, mDbHelper, mArgs ) ;

        projectList.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select project").setView(v);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}