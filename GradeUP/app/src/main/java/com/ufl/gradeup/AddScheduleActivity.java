package com.ufl.gradeup;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddScheduleActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    //Date picking and saving starts here ->
    public static TextView SelectedDateView;
    ImageButton addschbtn,cancelbtn;
    EditText subjectName;
    String date1,date2,date3,date4,date5,date6,date7;
    String regexSName = "[a-zA-Z0-9.-_@,/':()!#$%&*+\\s]{3,50}";
    int dayCheck, monthCheck, yearCheck;
    String dayofWeek;
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
    int startTimeInt=0,endTimeInt=0;
    int currentTimeInt=0;
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
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
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
            startTimeInt = hourOfDay*100 + minute + 1;
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
            endTimeInt = hourOfDay*100 + minute + 1;
            EndTimeView.setText("To " + endTime + " hrs");
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
        final String userName = currentUser.getString("username");
        addschbtn = (ImageButton) findViewById(R.id.addschbtn);
        cancelbtn = (ImageButton) findViewById(R.id.cancelBtn);
        SelectedDateView = (TextView) findViewById(R.id.start_date);
        StartTimeView = (TextView) findViewById(R.id.start_time);
        EndTimeView = (TextView) findViewById(R.id.end_time);
        subjectName = (EditText) findViewById(R.id.subjectname);
        RepeatToggle = (Switch) findViewById(R.id.repeatToggle);

        final TextView editText = (TextView) findViewById( R.id.start_date );
        final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        editText.setText("Date: " + sdf.format(new Date()));
        startDate = sdf.format(new Date());

        final TextView initialTime = (TextView) findViewById( R.id.start_time );
        final Calendar c1 = Calendar.getInstance();
        int hour = c1.get(Calendar.HOUR_OF_DAY);
        int minute = c1.get(Calendar.MINUTE);
        currentTimeInt = hour*100 + minute + 1;
        String editTime;
        if(minute/10 == 0){
            editTime = "" + hour + ":0" + minute + "";
        }
        else{
            editTime = "" + hour + ":" + minute + "";
        }
        initialTime.setText("From " + editTime + " hrs");
        startTime = editTime;

        final TextView initialEndTime = (TextView) findViewById( R.id.end_time );
        initialEndTime.setText("To  " + editTime + " hrs");
        endTime = editTime;

        subjectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern p = Pattern.compile(regexSName);
                Matcher m = p.matcher(subjectName.getText());
                if (!m.matches()) {
                    subjectName.setError("Course name has to be between 3 and 50 characters");
                    addschbtn.setEnabled(false);
                } else {
                    addschbtn.setEnabled(true);
                }
            }
        });

        SelectedDateView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                RepeatToggle.setChecked(false);
            }
        });


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

        RepeatToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                date1 = startDate;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cDate = Calendar.getInstance();
                try {
                    cDate.setTime(sdf.parse(startDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cDate.add(Calendar.DATE, 1);
                date2 = sdf.format(cDate.getTime());
                cDate.add(Calendar.DATE, 1);
                date3 = sdf.format(cDate.getTime());
                cDate.add(Calendar.DATE, 1);
                date4 = sdf.format(cDate.getTime());
                cDate.add(Calendar.DATE, 1);
                date5 = sdf.format(cDate.getTime());
                cDate.add(Calendar.DATE, 1);
                date6 = sdf.format(cDate.getTime());
                cDate.add(Calendar.DATE, 1);
                date7 = sdf.format(cDate.getTime());

                if (RepeatToggle.isChecked()) {
                    sunday.setClickable(true);
                    monday.setClickable(true);
                    tuesday.setClickable(true);
                    wednesday.setClickable(true);
                    thursday.setClickable(true);
                    friday.setClickable(true);
                    saturday.setClickable(true);
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
        cancelbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                finish();
            }
        });
        addschbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                String nameTxt = subjectName.getText().toString();
                if (nameTxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "A valid course name is required" ,
                            Toast.LENGTH_LONG).show();
                }
                else if(startTime == endTime)
                {
                    Toast.makeText(getApplicationContext(),"Start time & End time cannot be same",
                            Toast.LENGTH_LONG).show();
                }
                else if((startTimeInt >= endTimeInt) || (endTimeInt < currentTimeInt) || ((startTimeInt > currentTimeInt) && (endTimeInt == 0)))
                {
                    Toast.makeText(getApplicationContext(),"End time should be after start time",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    // Save new user data into Parse.com Data Storage
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
