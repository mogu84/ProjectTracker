package com.ville.devproc.projecttracker.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ville.devproc.prTracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int mChosenDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mChosenDate = bundle.getInt("DATE", 1);
        }


        switch (mChosenDate) {

            case R.id.pStartdate_view:
                return new DatePickerDialog(getActivity(),this, year, month, day);

            case R.id.pEnddate_view:
                return new DatePickerDialog(getActivity(), this, year, month, day);

        }
        return null;
    }



    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("fi", "FI"));
        calendar.set(year, month, day);

        switch( mChosenDate ) {

            case R.id.pStartdate_view:
                // set selected date into textview
                ((TextView) getActivity().findViewById(R.id.pStartdate_view)).setText(sdf.format(calendar.getTime()));
                break;

            case R.id.pEnddate_view:
                // set selected date into textview
                ((TextView) getActivity().findViewById(R.id.pEnddate_view)).setText(sdf.format(calendar.getTime()));
                break;
        }
    }
}