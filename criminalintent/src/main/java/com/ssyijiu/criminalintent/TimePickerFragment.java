package com.ssyijiu.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ssyijiu on 2017/4/24.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class TimePickerFragment extends DialogFragment {

    private static final String ARGS_CRIME_DATE = "args_crime_date";
    private static final String EXTRA_CRIME_DATE = "extra_crime_date";

    public static final int REQUEST_CRIME_DATE = 10;
    private Activity context;


    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }


    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 1. 获取 Date
        Date date = (Date) getArguments().getSerializable(ARGS_CRIME_DATE);

        // 2. 将 Date 转换成年月日
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        // 3. 初始化 TimePicker
        final TimePicker timePicker = (TimePicker) View.inflate(context, R.layout.dialog_timepicker,
            null);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.is24HourView();

        // 4. 创建 AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(android.R.string.cancel, null)
            .setView(timePicker);
        return builder.create();
    }


    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CRIME_DATE, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private void setResult(int resultCode, Date date) {
        if (getTargetFragment() != null) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_CRIME_DATE, date);
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        }
    }

    public static Date resultDate(Intent intent) {
        Date date = null;
        if(intent != null) {
            date = (Date) intent.getSerializableExtra(EXTRA_CRIME_DATE);
        }

        if(date != null) {
            return date;
        }
        return new Date();
    }
}
