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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddScheduleActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    //Date picking and saving starts here ->
    public static TextView SelectedDateView;
    Button addschbtn;
    EditText subjectName;
    int dayCheck, monthCheck, yearCheck;
    int hour;
    String dayofWeek;
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
            if(day/10 == 0){
                startDate = "" + year + "-" + (month + 1) + "-0" + day;
            }
            else{
                startDate = "" + year + "-" + (month + 1) + "-" + day;
            }
            SelectedDateView.setText("Date: " + startDate);
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
            if(minute/10 == 0){
                startTime = " " + hourOfDay + ":0" + minute + "";
            }
            else{
                startTime = " " + hourOfDay + ":" + minute + "";
            }
            StartTimeView.setText("From " + startTime + " hrs");
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
            if(minute/10 == 0){
                endTime = " " + hourOfDay + ":0" + minute + "";
            }
            else{
                endTime = " " + hourOfDay + ":" + minute + "";
            }
            EndTimeView.setText("To " + endTime + " hrs");
        }

    }
    //Time Picking and saving ends here


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        final TextView editText = (TextView) findViewById( R.id.start_date );
        final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        editText.setText("Date: " + sdf.format(new Date()));

        final TextView initialTime = (TextView) findViewById( R.id.start_time );
        SimpleDateFormat init = new SimpleDateFormat("HH:mm");
        String editTime = init.format(Calendar.getInstance().getTime());
        initialTime.setText("From " + editTime + " hrs");

        final TextView initialEndTime = (TextView) findViewById( R.id.end_time );
        SimpleDateFormat initEnd = new SimpleDateFormat("HH:mm");
        String editEndTime = initEnd.format(Calendar.getInstance().getTime());
        initialEndTime.setText("To  " + editEndTime + " hrs");

        RepeatToggle = (Switch) findViewById(R.id.repeatToggle);

        RepeatToggle.setClickable(true);
        final CheckBox sunday = (CheckBox) findViewById(R.id.Sunday);
        final CheckBox monday = (CheckBox) findViewById(R.id.Monday);
        final CheckBox tuesday = (CheckBox) findViewById(R.id.Tuesday);
        final CheckBox wednesday = (CheckBox) findViewById(R.id.Wednesday);
        final CheckBox thursday = (CheckBox) findViewById(R.id.Thursday);
        final CheckBox friday = (CheckBox) findViewById(R.id.Friday);
        final CheckBox saturday = (CheckBox) findViewById(R.id.Saturday);
        sunday.setClickable(false);
        monday.setClickable(false);
        tuesday.setClickable(false);
        wednesday.setClickable(false);
        thursday.setClickable(false);
        friday.setClickable(false);
        saturday.setClickable(false);

        ParseUser currentUser;
        currentUser = ParseUser.getCurrentUser();
        final String userName = currentUser.getString("username");
        addschbtn = (Button) findViewById(R.id.addschbtn);
        SelectedDateView = (TextView) findViewById(R.id.start_date);
        StartTimeView = (TextView) findViewById(R.id.start_time);
        EndTimeView = (TextView) findViewById(R.id.end_time);
        subjectName = (EditText) findViewById(R.id.subjectname);
        RepeatToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (RepeatToggle.isChecked()) {
                    switch (dayofWeek) {
                        case "Sunday":
                            sunday.setChecked(true);
                            break;
                        case "Monday":
                            monday.setChecked(true);
                            break;
                        case "Tuesday":
                            tuesday.setChecked(true);
                            break;
                        case "Wednesday":
                            wednesday.setChecked(true);
                            break;
                        case "Thursday":
                            thursday.setChecked(true);
                            break;
                        case "Friday":
                            friday.setChecked(true);
                            break;
                        case "Saturday":
                            saturday.setChecked(true);
                    }
                }
                else
                {   sunday.setChecked(false);
                    monday.setChecked(false);
                    tuesday.setChecked(false);
                    wednesday.setChecked(false);
                    thursday.setChecked(false);
                    friday.setChecked(false);
                    saturday.setChecked(false);

                }
            }
        });
        addschbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                String nameTxt = subjectName.getText().toString();
                if (nameTxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete course name field",
                            Toast.LENGTH_LONG).show();
                } else {
                    // Save new user data into Parse.com Data Storage
//                    SelectedDateView.setText(sdf.format(startDate));
//                    StartTimeView.setText(startTime);
//                    EndTimeView.setText(endTime);
                    ParseObject newSchedule = new ParseObject("Schedule");
                    newSchedule.put("User_ID", userName);
                    newSchedule.put("Subject", nameTxt);
                    newSchedule.put("Date", startDate);
                    newSchedule.put("Start_time", startTime);
                    newSchedule.put("End_time", endTime);
                    newSchedule.saveInBackground();
                    if (RepeatToggle.isChecked()) {
                        ParseObject newSchedule1 = new ParseObject("Schedule");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        try {
                            c.setTime(sdf.parse(startDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.add(Calendar.DATE, 7);
                        startDate = sdf.format(c.getTime());
                        newSchedule1.put("User_ID", userName);
                        newSchedule1.put("Subject", nameTxt);
                        newSchedule1.put("Date", startDate);
                        newSchedule1.put("Start_time", startTime);
                        newSchedule1.put("End_time", endTime);
                        newSchedule1.saveInBackground();
                        //-----------------------------------------------
                        ParseObject newSchedule2 = new ParseObject("Schedule");
                        try {
                            c.setTime(sdf.parse(startDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.add(Calendar.DATE, 7);
                        startDate = sdf.format(c.getTime());
                        newSchedule2.put("User_ID", userName);
                        newSchedule2.put("Subject", nameTxt);
                        newSchedule2.put("Date", startDate);
                        newSchedule2.put("Start_time", startTime);
                        newSchedule2.put("End_time", endTime);
                        newSchedule2.saveInBackground();
                        //-----------------------------------------------
                        ParseObject newSchedule3 = new ParseObject("Schedule");
                        try {
                            c.setTime(sdf.parse(startDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.add(Calendar.DATE, 7);
                        startDate = sdf.format(c.getTime());
                        newSchedule3.put("User_ID", userName);
                        newSchedule3.put("Subject", nameTxt);
                        newSchedule3.put("Date", startDate);
                        newSchedule3.put("Start_time", startTime);
                        newSchedule3.put("End_time", endTime);
                        newSchedule3.saveInBackground();
                        //-----------------------------------------------
                        ParseObject newSchedule4 = new ParseObject("Schedule");
                        try {
                            c.setTime(sdf.parse(startDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.add(Calendar.DATE, 7);
                        startDate = sdf.format(c.getTime());
                        newSchedule4.put("User_ID", userName);
                        newSchedule4.put("Subject", nameTxt);
                        newSchedule4.put("Date", startDate);
                        newSchedule4.put("Start_time", startTime);
                        newSchedule4.put("End_time", endTime);
                        newSchedule4.saveInBackground();
                    }
                    Intent intent = new Intent(AddScheduleActivity.this,
                            UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Successfully added.",
                            Toast.LENGTH_LONG).show();

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

