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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UpdateUserScheduleActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    //Date picking and saving starts here ->
    public static TextView SelectedDateView;
    Button addschbtn,delschbtn;
    EditText subjectName;
    int dayCheck, monthCheck, yearCheck;
    int hour;
    String dayofWeek, courseName, oldCourseName, oldStartDate, oldEndTime, oldStartTime;
    int minute;
    public static String startDate = "";
    private Switch RepeatToggle;

    public class DatePickerFragment extends DialogFragment
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
            dayCheck = day;
            monthCheck = month + 1;
            yearCheck = year;
            String input_date = "" + day + "/" + (month + 1) + "/" + year;
            SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
            Date dt1= null;
            try {
                dt1 = format1.parse(input_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat format2=new SimpleDateFormat("EEEE");
            dayofWeek=format2.format(dt1);
            Toast.makeText(getApplicationContext(), dayofWeek,
                    Toast.LENGTH_LONG).show();
            SelectedDateView.setText("Start Date " + startDate);
        }
    }
    //Date Picking and saving ends here


    //Time picking and saving starts here ->
    public static String startTime = "";
    public static String endTime = "";
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
            startTime = " " + hourOfDay + ":" + minute + "";
            StartTimeView.setText("Start Time " + startTime);
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
            endTime = " " + hourOfDay + ":" + minute ;
            EndTimeView.setText("End Time " + endTime);
        }

    }
    //Time Picking and saving ends here


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_schedule);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        ParseUser currentUser;
        currentUser = ParseUser.getCurrentUser();
        final String userEmail = currentUser.getString("username");

        Intent intentGet= getIntent();
        Bundle bundle = intentGet.getExtras();

        oldStartDate = bundle.getString("start_date");
        startDate = oldStartDate;
        oldStartTime = bundle.getString("Start_time");
        startTime = oldStartTime;
        oldEndTime = bundle.getString("End_time");
        endTime = oldEndTime;
        oldCourseName = bundle.getString("Subject");
        courseName = oldCourseName;

        final EditText initialSubjectName = (EditText) findViewById(R.id.subjectnameUpdate);
        initialSubjectName.setText(courseName);

        final TextView editText = (TextView) findViewById( R.id.start_dateUpdate );
        editText.setText("Start Date " + startDate);

        final TextView initialStartTime = (TextView) findViewById( R.id.start_timeUpdate );
        initialStartTime.setText("Start Time " + startTime);

        final TextView initialEndTime = (TextView) findViewById( R.id.end_timeUpdate );
        initialEndTime.setText("End Time " + endTime);

        addschbtn = (Button) findViewById(R.id.addschbtnUpdate);
        delschbtn = (Button) findViewById(R.id.delschbtnUpdate);
        SelectedDateView = (TextView) findViewById(R.id.start_dateUpdate);
        StartTimeView = (TextView) findViewById(R.id.start_timeUpdate);
        EndTimeView = (TextView) findViewById(R.id.end_timeUpdate);
        subjectName = (EditText) findViewById(R.id.subjectnameUpdate);

        // Delete selected data from Parse.com Data Storage
        delschbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Schedule");
                query1.whereEqualTo("User_ID", userEmail);
                query1.whereEqualTo("Subject", courseName);
                query1.whereEqualTo("End_time", endTime);
                query1.whereEqualTo("Start_time", startTime);
                query1.whereEqualTo("Date", startDate);
                query1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> nameList, com.parse.ParseException e1) {
                        if (e1 == null) {
                            for (ParseObject object : nameList) {
                                object.deleteInBackground();
                                object.saveInBackground();
                                Toast.makeText(getApplicationContext(), "Record Deleted",
                                Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
            }
        });

        addschbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Schedule");
                query1.whereEqualTo("User_ID", userEmail);
                query1.whereEqualTo("Subject", oldCourseName);
                query1.whereEqualTo("End_time", oldEndTime);
                query1.whereEqualTo("Start_time", oldStartTime);
                query1.whereEqualTo("Date", oldStartDate);
                query1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> nameList, com.parse.ParseException e1) {
                        if (e1 == null) {
                            for (ParseObject object : nameList) {
                                String nameTxt = subjectName.getText().toString();
                                object.put("Subject", nameTxt);
                                object.put("Date", startDate);
                                object.put("Start_time", startTime);
                                object.put("End_time", endTime);
                                object.saveInBackground();
                                Toast.makeText(getApplicationContext(), "Schedule Updated successfully",
                                        Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });


            }
        });
        Intent intent = new Intent(UpdateUserScheduleActivity.this,
                ViewScheduleActivity.class);
        startActivity(intent);
        finish();
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
