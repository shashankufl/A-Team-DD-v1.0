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
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddScheduleActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    //Date picking and saving starts here ->
    public static TextView SelectedDateView;
    ImageButton addschbtn,cancelbtn;
    int x=0,x1=0,x2=0,x3=0,x4=0,x5=0,x6=0,x7=0;
    EditText subjectName;
    String date1,date2,date3,date4,date5,date6,date7;
    String sunDate,monDate,tueDate,wedDate,thuDate,friDate,satDate;
    String regexSName = "[a-zA-Z0-9.-_@,/':()!#$%&*+\\s]{3,50}";
    int dayCheck, monthCheck, yearCheck;
    String dayofWeek,currentDate;
    public static String startDate = "";
    private Switch weeklyRepeatToggle,monthlyRepeatToggle;

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
        weeklyRepeatToggle = (Switch) findViewById(R.id.repeatToggle);
        monthlyRepeatToggle = (Switch) findViewById(R.id.repeatMonthToggle);
        weeklyRepeatToggle.setChecked(false);
        monthlyRepeatToggle.setChecked(false);
        final TextView monthlyRepeatText = (TextView) findViewById(R.id.monthlytext);

        final TextView editText = (TextView) findViewById( R.id.start_date );
        final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        editText.setText("Date: " + sdf.format(new Date()));
        startDate = sdf.format(new Date());
        currentDate = startDate;

        SimpleDateFormat formatDay = new SimpleDateFormat("EEEE", Locale.US);
        Calendar newInstance = Calendar.getInstance();
        dayofWeek = formatDay.format(newInstance.getTime());

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
                weeklyRepeatToggle.setChecked(false);
                monthlyRepeatToggle.setChecked(false);
            }
        });


        weeklyRepeatToggle.setClickable(true);
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


        weeklyRepeatToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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

                if (weeklyRepeatToggle.isChecked()) {
                    monthlyRepeatToggle.setChecked(false);
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
                            sunDate = date1;
                            monDate = date2;
                            tueDate = date3;
                            wedDate = date4;
                            thuDate = date5;
                            friDate = date6;
                            satDate = date7;
                            break;
                        case "Monday":
                            monday.setChecked(true);
                            sunDate = date7;
                            monDate = date1;
                            tueDate = date2;
                            wedDate = date3;
                            thuDate = date4;
                            friDate = date5;
                            satDate = date6;
                            break;
                        case "Tuesday":
                            tuesday.setChecked(true);
                            sunDate = date6;
                            monDate = date7;
                            tueDate = date1;
                            wedDate = date2;
                            thuDate = date3;
                            friDate = date4;
                            satDate = date5;
                            break;
                        case "Wednesday":
                            wednesday.setChecked(true);
                            sunDate = date5;
                            monDate = date6;
                            tueDate = date7;
                            wedDate = date1;
                            thuDate = date2;
                            friDate = date3;
                            satDate = date4;
                            break;
                        case "Thursday":
                            thursday.setChecked(true);
                            sunDate = date4;
                            monDate = date5;
                            tueDate = date6;
                            wedDate = date7;
                            thuDate = date1;
                            friDate = date2;
                            satDate = date3;
                            break;
                        case "Friday":
                            friday.setChecked(true);
                            sunDate = date3;
                            monDate = date4;
                            tueDate = date5;
                            wedDate = date6;
                            thuDate = date7;
                            friDate = date1;
                            satDate = date2;
                            break;
                        case "Saturday":
                            saturday.setChecked(true);
                            sunDate = date2;
                            monDate = date3;
                            tueDate = date4;
                            wedDate = date5;
                            thuDate = date6;
                            friDate = date7;
                            satDate = date1;
                    }
                } else {
                    sunday.setChecked(false);
                    monday.setChecked(false);
                    tuesday.setChecked(false);
                    wednesday.setChecked(false);
                    thursday.setChecked(false);
                    friday.setChecked(false);
                    saturday.setChecked(false);

                }
            }
        });

        monthlyRepeatToggle.setClickable(true);
        monthlyRepeatToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (monthlyRepeatToggle.isChecked()) {
                    x=0;x1=0;x2=0;x3=0;x4=0;x5=0;x6=0;x7=0;
                monthlyRepeatText.setText("        Schedule will repeat once every month");
                if(weeklyRepeatToggle.isChecked()){
                    weeklyRepeatToggle.setChecked(false);
                    x=1;
                }else{x=0;}
                     if(sunday.isChecked()){
                        sunday.setChecked(false);
                        x1=1;
                    }
                    if(monday.isChecked()){
                        monday.setChecked(false);
                        x2=1;
                    }
                    if(tuesday.isChecked()) {
                        tuesday.setChecked(false);
                        x3 = 1;
                    }
                    if(wednesday.isChecked()) {
                        wednesday.setChecked(false);
                        x4 = 1;
                    }
                    if(thursday.isChecked()) {
                        thursday.setChecked(false);
                        x5 = 1;
                    }
                    if(friday.isChecked()) {
                        friday.setChecked(false);
                        x6 = 1;
                    }
                    if(saturday.isChecked()) {
                        saturday.setChecked(false);
                        x7 = 1;
                    }
            }
                else
                {
                    monthlyRepeatText.setText(" ");
                    if(x==1){
                        weeklyRepeatToggle.setChecked(true);
                    }
                    if(x1==1){
                        sunday.setChecked(true);
                    }
                    if(x2==1){
                        monday.setChecked(true);
                    }
                    if(x3==1){
                        tuesday.setChecked(true);
                    }
                    if(x4==1){
                        wednesday.setChecked(true);
                    }
                    if(x5==1){
                        thursday.setChecked(true);
                    }
                    if(x6==1){
                        friday.setChecked(true);
                    }
                    if(x7==1) {
                        saturday.setChecked(false);
                    }
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
                                     "A valid course name is required",
                                     Toast.LENGTH_LONG).show();
                         } else if (startTime == endTime) {
                             Toast.makeText(getApplicationContext(), "Start time & End time cannot be same",
                                     Toast.LENGTH_LONG).show();
                         } else if ((startDate == currentDate) && ((startTimeInt >= endTimeInt) || (endTimeInt < currentTimeInt) || ((startTimeInt > currentTimeInt) && (endTimeInt == 0)))) {
                             Toast.makeText(getApplicationContext(), "End time should be after start time",
                                     Toast.LENGTH_LONG).show();
                         } else if ((startDate != currentDate) && ((startTimeInt >= endTimeInt) || (endTimeInt == 0))) {
                             Toast.makeText(getApplicationContext(), "End time should be after start time",
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
                             String actualStartDate = startDate;
                             newSchedule.saveInBackground();
                             if (weeklyRepeatToggle.isChecked()) {
                                 if (sunday.isChecked()) {

                                     startDate = actualStartDate;
                                     if (startDate != sunDate) {
                                         ParseObject newScheduleA = new ParseObject("Schedule");
                                         newScheduleA.put("User_ID", userName);
                                         newScheduleA.put("Subject", nameTxt);
                                         newScheduleA.put("Date", sunDate);
                                         newScheduleA.put("Start_time", startTime);
                                         newScheduleA.put("End_time", endTime);
                                         newScheduleA.saveInBackground();
                                     }
                                     startDate = sunDate;
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
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule4.put("User_ID", userName);
                                     newSchedule4.put("Subject", nameTxt);
                                     newSchedule4.put("Date", startDate);
                                     newSchedule4.put("Start_time", startTime);
                                     newSchedule4.put("End_time", endTime);
                                     newSchedule4.saveInBackground();

                                 }
                                 if (monday.isChecked()) {
                                     startDate = actualStartDate;
                                     if (startDate != monDate) {
                                         ParseObject newScheduleB = new ParseObject("Schedule");
                                         newScheduleB.put("User_ID", userName);
                                         newScheduleB.put("Subject", nameTxt);
                                         newScheduleB.put("Date", monDate);
                                         newScheduleB.put("Start_time", startTime);
                                         newScheduleB.put("End_time", endTime);
                                         newScheduleB.saveInBackground();
                                     }
                                     startDate = monDate;
                                     ParseObject newSchedule5 = new ParseObject("Schedule");
                                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                     Calendar c = Calendar.getInstance();
                                     try {
                                         c.setTime(sdf.parse(startDate));
                                     } catch (ParseException e) {
                                         e.printStackTrace();
                                     }
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule5.put("User_ID", userName);
                                     newSchedule5.put("Subject", nameTxt);
                                     newSchedule5.put("Date", startDate);
                                     newSchedule5.put("Start_time", startTime);
                                     newSchedule5.put("End_time", endTime);
                                     newSchedule5.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule6 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule6.put("User_ID", userName);
                                     newSchedule6.put("Subject", nameTxt);
                                     newSchedule6.put("Date", startDate);
                                     newSchedule6.put("Start_time", startTime);
                                     newSchedule6.put("End_time", endTime);
                                     newSchedule6.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule7 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule7.put("User_ID", userName);
                                     newSchedule7.put("Subject", nameTxt);
                                     newSchedule7.put("Date", startDate);
                                     newSchedule7.put("Start_time", startTime);
                                     newSchedule7.put("End_time", endTime);
                                     newSchedule7.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule8 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule8.put("User_ID", userName);
                                     newSchedule8.put("Subject", nameTxt);
                                     newSchedule8.put("Date", startDate);
                                     newSchedule8.put("Start_time", startTime);
                                     newSchedule8.put("End_time", endTime);
                                     newSchedule8.saveInBackground();

                                 }
                                 if (tuesday.isChecked()) {
                                     startDate = actualStartDate;
                                     if (startDate != tueDate) {
                                         ParseObject newScheduleC = new ParseObject("Schedule");
                                         newScheduleC.put("User_ID", userName);
                                         newScheduleC.put("Subject", nameTxt);
                                         newScheduleC.put("Date", tueDate);
                                         newScheduleC.put("Start_time", startTime);
                                         newScheduleC.put("End_time", endTime);
                                         newScheduleC.saveInBackground();
                                     }
                                     startDate = tueDate;
                                     ParseObject newSchedule9 = new ParseObject("Schedule");
                                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                     Calendar c = Calendar.getInstance();
                                     try {
                                         c.setTime(sdf.parse(startDate));
                                     } catch (ParseException e) {
                                         e.printStackTrace();
                                     }
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule9.put("User_ID", userName);
                                     newSchedule9.put("Subject", nameTxt);
                                     newSchedule9.put("Date", startDate);
                                     newSchedule9.put("Start_time", startTime);
                                     newSchedule9.put("End_time", endTime);
                                     newSchedule9.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule10 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule10.put("User_ID", userName);
                                     newSchedule10.put("Subject", nameTxt);
                                     newSchedule10.put("Date", startDate);
                                     newSchedule10.put("Start_time", startTime);
                                     newSchedule10.put("End_time", endTime);
                                     newSchedule10.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule11 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule11.put("User_ID", userName);
                                     newSchedule11.put("Subject", nameTxt);
                                     newSchedule11.put("Date", startDate);
                                     newSchedule11.put("Start_time", startTime);
                                     newSchedule11.put("End_time", endTime);
                                     newSchedule11.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule12 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule12.put("User_ID", userName);
                                     newSchedule12.put("Subject", nameTxt);
                                     newSchedule12.put("Date", startDate);
                                     newSchedule12.put("Start_time", startTime);
                                     newSchedule12.put("End_time", endTime);
                                     newSchedule12.saveInBackground();

                                 }
                                 if (wednesday.isChecked()) {
                                     startDate = actualStartDate;
                                     if (startDate != wedDate) {
                                         ParseObject newScheduleD = new ParseObject("Schedule");
                                         newScheduleD.put("User_ID", userName);
                                         newScheduleD.put("Subject", nameTxt);
                                         newScheduleD.put("Date", wedDate);
                                         newScheduleD.put("Start_time", startTime);
                                         newScheduleD.put("End_time", endTime);
                                         newScheduleD.saveInBackground();
                                     }
                                     startDate = wedDate;
                                     ParseObject newSchedule13 = new ParseObject("Schedule");
                                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                     Calendar c = Calendar.getInstance();
                                     try {
                                         c.setTime(sdf.parse(startDate));
                                     } catch (ParseException e) {
                                         e.printStackTrace();
                                     }
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule13.put("User_ID", userName);
                                     newSchedule13.put("Subject", nameTxt);
                                     newSchedule13.put("Date", startDate);
                                     newSchedule13.put("Start_time", startTime);
                                     newSchedule13.put("End_time", endTime);
                                     newSchedule13.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule14 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule14.put("User_ID", userName);
                                     newSchedule14.put("Subject", nameTxt);
                                     newSchedule14.put("Date", startDate);
                                     newSchedule14.put("Start_time", startTime);
                                     newSchedule14.put("End_time", endTime);
                                     newSchedule14.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule15 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule15.put("User_ID", userName);
                                     newSchedule15.put("Subject", nameTxt);
                                     newSchedule15.put("Date", startDate);
                                     newSchedule15.put("Start_time", startTime);
                                     newSchedule15.put("End_time", endTime);
                                     newSchedule15.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule16 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule16.put("User_ID", userName);
                                     newSchedule16.put("Subject", nameTxt);
                                     newSchedule16.put("Date", startDate);
                                     newSchedule16.put("Start_time", startTime);
                                     newSchedule16.put("End_time", endTime);
                                     newSchedule16.saveInBackground();

                                 }
                                 if (thursday.isChecked()) {
                                     startDate = actualStartDate;
                                     if (startDate != thuDate) {
                                         ParseObject newScheduleE = new ParseObject("Schedule");
                                         newScheduleE.put("User_ID", userName);
                                         newScheduleE.put("Subject", nameTxt);
                                         newScheduleE.put("Date", thuDate);
                                         newScheduleE.put("Start_time", startTime);
                                         newScheduleE.put("End_time", endTime);
                                         newScheduleE.saveInBackground();
                                     }
                                     startDate = thuDate;
                                     ParseObject newSchedule17 = new ParseObject("Schedule");
                                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                     Calendar c = Calendar.getInstance();
                                     try {
                                         c.setTime(sdf.parse(startDate));
                                     } catch (ParseException e) {
                                         e.printStackTrace();
                                     }
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule17.put("User_ID", userName);
                                     newSchedule17.put("Subject", nameTxt);
                                     newSchedule17.put("Date", startDate);
                                     newSchedule17.put("Start_time", startTime);
                                     newSchedule17.put("End_time", endTime);
                                     newSchedule17.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule18 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule18.put("User_ID", userName);
                                     newSchedule18.put("Subject", nameTxt);
                                     newSchedule18.put("Date", startDate);
                                     newSchedule18.put("Start_time", startTime);
                                     newSchedule18.put("End_time", endTime);
                                     newSchedule18.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule19 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule19.put("User_ID", userName);
                                     newSchedule19.put("Subject", nameTxt);
                                     newSchedule19.put("Date", startDate);
                                     newSchedule19.put("Start_time", startTime);
                                     newSchedule19.put("End_time", endTime);
                                     newSchedule19.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule20 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule20.put("User_ID", userName);
                                     newSchedule20.put("Subject", nameTxt);
                                     newSchedule20.put("Date", startDate);
                                     newSchedule20.put("Start_time", startTime);
                                     newSchedule20.put("End_time", endTime);
                                     newSchedule20.saveInBackground();

                                 }
                                 if (friday.isChecked()) {
                                     startDate = actualStartDate;
                                     if (startDate != friDate) {
                                         ParseObject newScheduleF = new ParseObject("Schedule");
                                         newScheduleF.put("User_ID", userName);
                                         newScheduleF.put("Subject", nameTxt);
                                         newScheduleF.put("Date", friDate);
                                         newScheduleF.put("Start_time", startTime);
                                         newScheduleF.put("End_time", endTime);
                                         newScheduleF.saveInBackground();
                                     }
                                     startDate = friDate;
                                     ParseObject newSchedule21 = new ParseObject("Schedule");
                                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                     Calendar c = Calendar.getInstance();
                                     try {
                                         c.setTime(sdf.parse(startDate));
                                     } catch (ParseException e) {
                                         e.printStackTrace();
                                     }
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule21.put("User_ID", userName);
                                     newSchedule21.put("Subject", nameTxt);
                                     newSchedule21.put("Date", startDate);
                                     newSchedule21.put("Start_time", startTime);
                                     newSchedule21.put("End_time", endTime);
                                     newSchedule21.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule22 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule22.put("User_ID", userName);
                                     newSchedule22.put("Subject", nameTxt);
                                     newSchedule22.put("Date", startDate);
                                     newSchedule22.put("Start_time", startTime);
                                     newSchedule22.put("End_time", endTime);
                                     newSchedule22.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule23 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule23.put("User_ID", userName);
                                     newSchedule23.put("Subject", nameTxt);
                                     newSchedule23.put("Date", startDate);
                                     newSchedule23.put("Start_time", startTime);
                                     newSchedule23.put("End_time", endTime);
                                     newSchedule23.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule24 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule24.put("User_ID", userName);
                                     newSchedule24.put("Subject", nameTxt);
                                     newSchedule24.put("Date", startDate);
                                     newSchedule24.put("Start_time", startTime);
                                     newSchedule24.put("End_time", endTime);
                                     newSchedule24.saveInBackground();

                                 }
                                 if (saturday.isChecked()) {
                                     startDate = actualStartDate;
                                     if (startDate != satDate) {
                                         ParseObject newScheduleG = new ParseObject("Schedule");
                                         newScheduleG.put("User_ID", userName);
                                         newScheduleG.put("Subject", nameTxt);
                                         newScheduleG.put("Date", satDate);
                                         newScheduleG.put("Start_time", startTime);
                                         newScheduleG.put("End_time", endTime);
                                         newScheduleG.saveInBackground();
                                     }
                                     startDate = satDate;
                                     ParseObject newSchedule25 = new ParseObject("Schedule");
                                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                     Calendar c = Calendar.getInstance();
                                     try {
                                         c.setTime(sdf.parse(startDate));
                                     } catch (ParseException e) {
                                         e.printStackTrace();
                                     }
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule25.put("User_ID", userName);
                                     newSchedule25.put("Subject", nameTxt);
                                     newSchedule25.put("Date", startDate);
                                     newSchedule25.put("Start_time", startTime);
                                     newSchedule25.put("End_time", endTime);
                                     newSchedule25.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule26 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule26.put("User_ID", userName);
                                     newSchedule26.put("Subject", nameTxt);
                                     newSchedule26.put("Date", startDate);
                                     newSchedule26.put("Start_time", startTime);
                                     newSchedule26.put("End_time", endTime);
                                     newSchedule26.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule27 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule27.put("User_ID", userName);
                                     newSchedule27.put("Subject", nameTxt);
                                     newSchedule27.put("Date", startDate);
                                     newSchedule27.put("Start_time", startTime);
                                     newSchedule27.put("End_time", endTime);
                                     newSchedule27.saveInBackground();
                                     //-----------------------------------------------
                                     ParseObject newSchedule28 = new ParseObject("Schedule");
                                     c.add(Calendar.DATE, 7);
                                     startDate = sdf.format(c.getTime());
                                     newSchedule28.put("User_ID", userName);
                                     newSchedule28.put("Subject", nameTxt);
                                     newSchedule28.put("Date", startDate);
                                     newSchedule28.put("Start_time", startTime);
                                     newSchedule28.put("End_time", endTime);
                                     newSchedule28.saveInBackground();

                                 }

                             }
                             else if(monthlyRepeatToggle.isChecked()){

                                 ParseObject newSchedule1 = new ParseObject("Schedule");
                                 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                 Calendar c = Calendar.getInstance();
                                 try {
                                     c.setTime(sdf.parse(startDate));
                                 } catch (ParseException e) {
                                     e.printStackTrace();
                                 }
                                 c.add(Calendar.DATE, 30);
                                 startDate = sdf.format(c.getTime());
                                 newSchedule1.put("User_ID", userName);
                                 newSchedule1.put("Subject", nameTxt);
                                 newSchedule1.put("Date", startDate);
                                 newSchedule1.put("Start_time", startTime);
                                 newSchedule1.put("End_time", endTime);
                                 newSchedule1.saveInBackground();
                                 //-----------------------------------//
                                 ParseObject newSchedule2 = new ParseObject("Schedule");
                                 c.add(Calendar.DATE, 30);
                                 startDate = sdf.format(c.getTime());
                                 newSchedule2.put("User_ID", userName);
                                 newSchedule2.put("Subject", nameTxt);
                                 newSchedule2.put("Date", startDate);
                                 newSchedule2.put("Start_time", startTime);
                                 newSchedule2.put("End_time", endTime);
                                 newSchedule2.saveInBackground();
                                 //-----------------------------------//
                                 ParseObject newSchedule3 = new ParseObject("Schedule");
                                 c.add(Calendar.DATE, 30);
                                 startDate = sdf.format(c.getTime());
                                 newSchedule3.put("User_ID", userName);
                                 newSchedule3.put("Subject", nameTxt);
                                 newSchedule3.put("Date", startDate);
                                 newSchedule3.put("Start_time", startTime);
                                 newSchedule3.put("End_time", endTime);
                                 newSchedule3.saveInBackground();
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
                 if (id == R.id.resetOption) {
                     onResetOptionClicked();
                 }

                 return super.onOptionsItemSelected(item);
             }

    //To refresh the current page
    public void onResetOptionClicked() {
        Intent intent = new Intent(AddScheduleActivity.this,
                AddScheduleActivity.class);
        startActivity(intent);
        finish();
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
