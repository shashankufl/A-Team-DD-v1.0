package com.ufl.gradeup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateGroupMeetingActivity extends AppCompatActivity {

    static final int start_dialog_id = 0;
    static final int end_dialog_id = 1;
    int startHour, startMinute, endHour, endMinute;
    EditText meetingAgendaTxt;
    EditText meetingTitleTxt;
    TextView meetingStartTimeBtn;
    TextView meetingEndTimeBtn;
    private ArrayList<String> groupUserNameList = new ArrayList<String>();
    int freeSlotSize;


    private android.support.v7.widget.Toolbar toolbar;
    private android.support.v7.widget.CardView freeSlotCard;
    private android.support.v7.widget.CardView timeCard;
    private TextView getTimeTextView;
    private ArrayList<String> groupMembersList = new ArrayList<String>();
    private ArrayList<String> startTimeList = new ArrayList<String>();
    private ArrayList<String> endTimeList = new ArrayList<String>();
    private ArrayList<Double> normStartTime = new ArrayList<Double>();
    private ArrayList<Double> normEndTime = new ArrayList<Double>();
    private ArrayList<Integer> timeSlotArray = new ArrayList<Integer>();
    private ArrayList<String> freeSlotList = new ArrayList<String>();
    private ArrayList<String> startTimeSlotList = new ArrayList<String>();
    private ArrayList<String> endTimeSlotList = new ArrayList<String>();
    public static String selectedDate = "";
    public static TextView SelectedDateView;
    public static TextView StartTimeView;
    public static TextView EndTimeView;
    String groupName;
    boolean flag = true;

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
            String monthTxt = Integer.toString(month+1);
            String dayTxt = Integer.toString(day);
            if(day<10){
                dayTxt = "0"+dayTxt;
            }
            if(month<10){
                monthTxt = "0"+monthTxt;
            }
            selectedDate = "" + year + "-" + monthTxt + "-" + dayTxt;
            SelectedDateView.setText(selectedDate);
        }

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_meeting);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        freeSlotCard = (android.support.v7.widget.CardView) findViewById(R.id.availableTimeCard);
        timeCard = (android.support.v7.widget.CardView) findViewById(R.id.startTimeCard);
        meetingAgendaTxt = (EditText) findViewById(R.id.meetingAgenda);
        meetingStartTimeBtn = (TextView) findViewById(R.id.meetingStartTimeButton);
        meetingEndTimeBtn = (TextView) findViewById(R.id.meetingEndTimeButton);
        meetingTitleTxt = (EditText) findViewById(R.id.meetingTitle);
        StartTimeView = (TextView) findViewById(R.id.meetingStartTimeButton);
        EndTimeView = (TextView) findViewById(R.id.meetingEndTimeButton);
        groupUserNameList = getIntent().getStringArrayListExtra("memberList");
        groupName = getIntent().getStringExtra("groupName");
        this.setTitle("New Meeting for "+groupName);
        freeSlotCard.setVisibility(View.GONE);
        timeCard.setVisibility(View.GONE);
        meetingStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(start_dialog_id);
            }
        });

        meetingEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(end_dialog_id);
            }
        });

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = simpleDateFormat.format(new Date());
        SelectedDateView = (TextView) findViewById(R.id.selectedDateTxt);
        SelectedDateView.setText(selectedDate);
        groupMembersList = getIntent().getStringArrayListExtra("memberList");

        for (int i = 0; i < 48; i++) {
            timeSlotArray.add(0);
        }
    }

    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        if (id == start_dialog_id) {
            return new TimePickerDialog(this, StartTimeSetListener, startHour, startMinute, true);
        } else if (id == end_dialog_id) {
            return new TimePickerDialog(this, EndTimeSetListener, endHour, endMinute, true);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener StartTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    startHour = hourOfDay;
                    startMinute = minute;
                    String hr = Integer.toString(hourOfDay);
                    String min = Integer.toString(minute);
                    if (hourOfDay < 10) {
                        hr = "0" + hourOfDay;
                    }
                    if (minute < 10) {
                        min = "0" + minute;
                    }
                    StartTimeView.setText(hr + ":" + min);
//                    Toast.makeText(getApplicationContext(),
//                            startHour + ":" + startMinute,
//                            Toast.LENGTH_SHORT).show();
                }
            };
    private TimePickerDialog.OnTimeSetListener EndTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    endHour = hourOfDay;
                    endMinute = minute;
                    String hr = Integer.toString(hourOfDay);
                    String min = Integer.toString(minute);
                    if (hourOfDay < 10) {
                        hr = "0" + hourOfDay;
                    }
                    if (minute < 10) {
                        min = "0" + minute;
                    }
                    EndTimeView.setText(hr + ":" + min);
//                    Toast.makeText(getApplicationContext(),
//                            endHour + ":" + endMinute,
//                            Toast.LENGTH_SHORT).show();
                }
            };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group_meeting, menu);
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

        if (id == R.id.createMeeting) {
            if (isWithinFreeSlot()) {
                createMeeting();
                addMeetingToSchedule();
                Intent intent = new Intent(CreateGroupMeetingActivity.this,
                        GroupHomeActivity.class);
                intent.putExtra("groupName", groupName);
                startActivity(intent);

                finish();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Please enter a time between the free slot",
                        Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void getSelectedDateSchedule(View v) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
        query.whereContainedIn("User_ID", groupMembersList);
        final ProgressDialog freeSlotProgress = new ProgressDialog(this);
        freeSlotProgress.setTitle("Searching for Free time slots...");
        freeSlotProgress.show();
        //int n = query.count();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> nameList, ParseException e) {
                if (e == null) {
                    for (ParseObject object : nameList) {
                        if (new String(object.getString("Date")).equals(selectedDate)) {
                            startTimeList.add(object.getString("Start_time"));
                            endTimeList.add(object.getString("End_time"));
                        }
                    }
                    //SelectedDateView.setText(startTimeList.toString());
                    if(flag){
                        flag =false;
                        normalizeTime();
                    }

                    freeSlotProgress.dismiss();
                }
            }
        });

    }

    public void normalizeTime() {
        for (int i = 0; i < startTimeList.size(); i++) {
            if (Integer.parseInt(startTimeList.get(i).split(":")[1]) < 30) {
                normStartTime.add(i, Double.parseDouble(startTimeList.get(i).split(":")[0]));
            } else {
                normStartTime.add(i, Double.parseDouble(startTimeList.get(i).split(":")[0]) + 0.5);
            }
            if (Integer.parseInt(endTimeList.get(0).split(":")[1]) < 30) {
                normEndTime.add(i, Double.parseDouble(endTimeList.get(i).split(":")[0]) + 0.5);
            } else {
                normEndTime.add(i, Double.parseDouble(endTimeList.get(i).split(":")[0]) + 1.0);
            }
        }

        for (int j = 0; j < normStartTime.size(); j++) {
            if (normEndTime.get(j) - normStartTime.get(j) > 1.0) {
                for (double c = normStartTime.get(j) * 2; c < normEndTime.get(j) * 2; c++) {
                    timeSlotArray.set((int) c, 1);
                }
            } else {
                timeSlotArray.set((int) (normStartTime.get(j) * 2), 1);
            }
        }
        //getFreeSlots();
        getfreeTime();
        bindFreeSlots();
        freeSlotCard.setVisibility(View.VISIBLE);
        timeCard.setVisibility(View.VISIBLE);
        getTimeTextView = (TextView) findViewById(R.id.getTimeTxt);
        getTimeTextView.setVisibility(View.GONE);

        //SelectedDateView.setText(freeSlotList.toString());
    }

    private void getfreeTime() {

        float startTime;
        float endTime;
        String startTimeString;
        String endTimeString;

        for (int i = 0; i < timeSlotArray.size(); i++) {
            if (timeSlotArray.get(i) == 0) {
                startTime = (float) (i * .5);
                startTimeString = convertToTimeSlot(startTime);
                int zeroCounter = 0;
                for (int j = i; j < timeSlotArray.size(); j++) {
                    zeroCounter++;
                    if (timeSlotArray.get(j) == 1) {
                        break;
                    }
                }
                endTime = (float) ((i + zeroCounter - 1) * .5);
                endTimeString = convertToTimeSlot(endTime);
                i = i + zeroCounter - 1;

                freeSlotList.add(startTimeString + " to " + endTimeString);
                startTimeSlotList.add(startTimeString);
                endTimeSlotList.add(endTimeString);
            }

        }
        freeSlotSize = freeSlotList.size();

    }

    private String convertToTimeSlot(float decimalTime) {
        String timeSlot;
        float fractionalPart = decimalTime % 1;
        int integralPart = (int) (decimalTime - fractionalPart);

        if (fractionalPart == .5) {
            timeSlot = integralPart + ":30";
        } else {
            timeSlot = integralPart + ":00";
        }

        if (integralPart < 10) {
            timeSlot = "0" + timeSlot;
        }

        return timeSlot;

    }

    public void bindFreeSlots() {
        LinearLayout freeSlotLayout = (LinearLayout) findViewById(R.id.freeSlotLinearLayout);
        if (freeSlotList.size() == 0) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setTypeface(textView.getTypeface(), Typeface.ITALIC);
            textView.setText("Nothing to Show...");
            if (Build.VERSION.SDK_INT < 23) {
                textView.setTextAppearance(this, android.R.style.TextAppearance_Material_Small);
            } else {
                textView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
            }
            freeSlotLayout.addView(textView);
        } else {
            for (int i = 0; i < freeSlotList.size(); i++) {
                TextView titletextView = new TextView(this);
                TextView timeTextView = new TextView(this);

                titletextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                titletextView.setText(freeSlotList.get(i));
                titletextView.setFocusable(true);
                titletextView.setClickable(true);

//                timeTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT));
//                timeTextView.setText(freeSlotList.get(i));
//                timeTextView.setFocusable(true);
//                timeTextView.setClickable(true);


                if (Build.VERSION.SDK_INT < 23) {
                    titletextView.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
                    //timeTextView.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
                } else {
                    titletextView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
                    //timeTextView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
                }

                freeSlotLayout.addView(titletextView);
                //freeSlotLayout.addView(timeTextView);

//                if (i < freeSlotList.size() - 1) {
//                    View view = new View(this);
//                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1);
//                    params.setMargins(0, 15, 0, 15);
//                    view.setLayoutParams(params);
//                    view.setBackgroundColor(Color.rgb(169, 169, 169));
//                    freeSlotLayout.addView(view);
//                }
            }
        }


    }

    private void createMeeting() {
        String Agenda = meetingAgendaTxt.getText().toString();
        String MeetingTitle = meetingTitleTxt.getText().toString();
        String startMinuteTxt = Integer.toString(startMinute);
        String endMinuteTxt = Integer.toString(endMinute);
        String startHourTxt = Integer.toString(startHour);
        String endHourTxt = Integer.toString(endHour);
        if(startMinute<10){
            startMinuteTxt = "0"+startMinuteTxt;
        }
        if(endMinute<10){
            endMinuteTxt = "0"+endMinuteTxt;
        }
        if(startHour<10){
            startHourTxt = "0"+startHourTxt;
        }
        if(endHour<10){
            endHourTxt = "0"+endHourTxt;
        }

        String meetingStartTime = startHourTxt + ":" + startMinuteTxt;
        String meetingEndTime = endHourTxt + ":" + endMinuteTxt;
        ParseObject groupMeeting = new ParseObject("MemberSchedule");
        groupMeeting.put("meetingStartTime", meetingStartTime);
        groupMeeting.put("meetingEndTime", meetingEndTime);
        groupMeeting.put("Agenda", Agenda);
        groupMeeting.put("meetingTitle", MeetingTitle);
        groupMeeting.put("Date", selectedDate);
        groupMeeting.put("groupName", getIntent().getStringExtra("groupName"));
        ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
        acl.setPublicWriteAccess(true);
        acl.setPublicReadAccess(true);
        groupMeeting.setACL(acl);
        groupMeeting.saveInBackground();


    }

    private void addMeetingToSchedule() {
        ParseObject schedule;
        String startMinuteTxt = Integer.toString(startMinute);
        String endMinuteTxt = Integer.toString(endMinute);
        String startHourTxt = Integer.toString(startHour);
        String endHourTxt = Integer.toString(endHour);
        if(startMinute<10){
            startMinuteTxt = "0"+startMinuteTxt;
        }
        if(endMinute<10){
            endMinuteTxt = "0"+endMinuteTxt;
        }
        if(startHour<10){
            startHourTxt = "0"+startHourTxt;
        }
        if(endHour<10){
            endHourTxt = "0"+endHourTxt;
        }

        String meetingStartTime = startHour + ":" + startMinute;
        String meetingEndTime = endHour + ":" + endMinute;
        String Agenda = meetingAgendaTxt.getText().toString();
        String MeetingTitle = meetingTitleTxt.getText().toString();

        for (int i = 0; i < groupUserNameList.size(); i++) {
            schedule = new ParseObject("Schedule");
            schedule.put("User_ID", groupUserNameList.get(i));
            schedule.put("Start_time", meetingStartTime);
            schedule.put("End_time", meetingEndTime);
            schedule.put("Subject", MeetingTitle);
            schedule.put("Date", selectedDate);
            schedule.saveInBackground();
        }

    }

    private boolean isWithinFreeSlot() {
        String meetingStartTime = startHour + ":" + startMinute;
        String meetingEndTime = endHour + ":" + endMinute;
        boolean isWithinTimeSlot = false;

        DateFormat formatter = new SimpleDateFormat("hh:mm");
        try {
            Date startTime = formatter.parse(meetingStartTime);
            Date endTime = formatter.parse(meetingEndTime);


            for (int i = 0; i < freeSlotSize; i++) {
                Date freeStartTime = formatter.parse(startTimeSlotList.get(i));
                Date freeEndTime = formatter.parse(endTimeSlotList.get(i));
                if (startTime.after(freeStartTime) && endTime.before(freeEndTime)) {
                    isWithinTimeSlot = true;
                }
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        return isWithinTimeSlot;
    }
//    public void getFreeSlots(){
//        int k=0;
//        while(k<48){
//            if(timeSlotsArray.get(k)==1){
//                k++;
//            }else if(timeSlotsArray.get(k)==0){
//                int curretSlotLength=0;
//                Double slotStart = (k/2.0);
//                while (k<48 && timeSlotsArray.get(k)==0){
//                    curretSlotLength++;
//                    k++;
//                }
//                Double slotEnd= slotStart+0.5*curretSlotLength;
//                String slotStartTxt="";
//                String slotEndTxt="";
//                if(slotStart%2!=0){
//                    slotStartTxt = slotStart.intValue()+":30";
//                }else{
//                    slotStartTxt = slotStart.intValue()+":00";
//                }
//
//                if(slotEnd%2!=0){
//                    slotEndTxt = slotEnd.intValue()+":30";
//                }else{
//                    slotEndTxt = slotEnd.intValue()+":00";
//                }
//                freeSlotList.add(slotStartTxt + " to "+ slotEndTxt);
//
//            }
//        }
//        SelectedDateView.setText(freeSlotList.toString());
//    }

}
