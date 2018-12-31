package com.ville.devproc.projecttracker.ui.Timesheet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Calendar;
import java.util.TimeZone;

public class AddTimesheetDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialogListener mListener;
    private DateTime mDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        // Use the current date as the default date in the picker
        // mDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        if( bundle.containsKey("date")) {
            try {
                mDate = (DateTime) bundle.getSerializable("date");
            } catch (Exception e) {

            }
        }

        mDate.toDateTime(DateTimeZone.UTC);

        int year = mDate.getYear();
        int month = mDate.getMonthOfYear() -1;
        int day = mDate.getDayOfMonth();

        return new DatePickerDialog(getActivity(),this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(year, month, day,0 , 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        mListener.onFinishDatePickerDialogListener(Long.toString( calendar.getTimeInMillis() ) );
        this.dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            mListener = (DatePickerDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement EditNameDialogListener");
        }
    }

    public interface DatePickerDialogListener {
        void onFinishDatePickerDialogListener(String inputText);
    }
}
