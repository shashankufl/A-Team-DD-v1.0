package com.ufl.gradeup;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    //Date picking and saving starts here ->
    public static TextView SelectedDateView;
    Button addschbtn;
    EditText subjectName;
    int hour;
    int minute;
    public static String startDate = "";

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as default
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Creating new instance of DatePickerDialog
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            startDate = "" + year + "-" + (month + 1) + "-" + day;
            SelectedDateView.setText(startDate);

        }
    }
    //Date Picking and saving ends here


    //Time picking and saving starts here ->
    public static String startTime = "";
    public static String endTime = "";
    //public static String setTime = "";
    public static TextView EndTimeView;
    public static TextView StartTimeView;

    public class TimePickerFragment1 extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as default
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            // Creating new instance of DatePickerDialog
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startTime = " " + hourOfDay + ":" + minute + ":00";
            StartTimeView.setText(startTime);
        }

    }

    public class TimePickerFragment2 extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as default
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            // Creating new instance of DatePickerDialog
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endTime = " " + hourOfDay + ":" + minute + ":00";
            EndTimeView.setText(endTime);
        }

    }
    //Time Picking and saving ends here



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        ParseUser currentUser;
        currentUser = ParseUser.getCurrentUser();
        final String userName = currentUser.getString("Name");

        addschbtn = (Button) findViewById(R.id.addschbtn);
        SelectedDateView = (TextView) findViewById(R.id.start_date);
        StartTimeView = (TextView) findViewById(R.id.start_time);
        EndTimeView = (TextView) findViewById(R.id.end_time);
        subjectName = (EditText) findViewById(R.id.subjectname);
        addschbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                String nameTxt = subjectName.getText().toString();
                if (nameTxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete course name field",
                            Toast.LENGTH_LONG).show();
                } else {
                    // Save new user data into Parse.com Data Storage
                    ParseObject newSchedule = new ParseObject("Schedule");
                    newSchedule.put("User_ID", userName);
                    newSchedule.put("Subject", nameTxt);
                    newSchedule.put("Date", startDate);
                    newSchedule.put("Start_time", startTime);
                    newSchedule.put("End_time", endTime);
                    newSchedule.saveInBackground();
                    Intent intent = new Intent(AddScheduleActivity.this,
                            UserProfileActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Successfully updated.",
                            Toast.LENGTH_LONG).show();
                    finish();

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog1(View v) {
        DialogFragment newFragment1 = new TimePickerFragment1();
        newFragment1.show(getSupportFragmentManager(), "timePicker");
    }

    public void showTimePickerDialog2(View v) {
        DialogFragment newFragment2 = new TimePickerFragment2();
        newFragment2.show(getSupportFragmentManager(), "timePicker");
    }
}

