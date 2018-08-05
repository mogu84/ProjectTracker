package com.ville.devproc.projecttracker.ui.Project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.ville.devproc.prTracker.R;
import com.ville.devproc.projecttracker.data.db.DBHelper;
import com.ville.devproc.projecttracker.data.db.model.Project;
import com.ville.devproc.projecttracker.data.db.model.Worker;

import java.util.List;

public class SelectWorkerDialogFragment extends DialogFragment {
    private SelectWorkerListViewAdapter adapter;
    private DBHelper mDbHelper;
    private Bundle mArgs;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mArgs = getArguments();

        mDbHelper = new DBHelper(getActivity().getApplicationContext());
        List<Worker> workers = mDbHelper.getAllUnassignedWorkers(mArgs.getInt(Project.COLUMN_PROJECT_ID));

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_itemselect, null);

        final EditText filterText = v.findViewById(R.id.pFilterByName);
        filterText.setHint("Filter worker names");
        ListView listView = v.findViewById(R.id.dSelectListView);

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

        adapter = new SelectWorkerListViewAdapter( getActivity().getApplicationContext(), workers, mDbHelper, mArgs ) ;

        listView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select worker").setView(v);

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