package com.ufl.gradeup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CreateGroupMeetingActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private ArrayList<String> groupMembersList = new ArrayList<String>();
    private ArrayList<String> startTimeList = new ArrayList<String>();
    private ArrayList<String> endTimeList = new ArrayList<String>();
    private ArrayList<Double> normStartTime = new ArrayList<Double>();
    private ArrayList<Double> normEndTime = new ArrayList<Double>();
    private ArrayList<Integer> timeSlotArray = new ArrayList<Integer>();
    private ArrayList<String> freeSlotList = new ArrayList<String>();
    public static String selectedDate = "";
    public static TextView SelectedDateView;

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
            selectedDate = "" + year + "-" + (month + 1) + "-" + day;
            SelectedDateView.setText(selectedDate);
        }

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_meeting);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        SelectedDateView = (TextView) findViewById(R.id.selectedDateTxt);
        groupMembersList = getIntent().getStringArrayListExtra("memberList");

        for (int i = 0; i < 48; i++) {
            timeSlotArray.add(0);
        }
    }

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

        return super.onOptionsItemSelected(item);
    }

    public void getSelectedDateSchedule(View v) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
        query.whereContainedIn("User_ID", groupMembersList);
        int n = query.count();
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
                    SelectedDateView.setText(startTimeList.toString());
                    normalizeTime();

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
                for (double c = normStartTime.get(j)*2; c < normEndTime.get(j)*2; c++) {
                    timeSlotArray.set((int)c,1);
                }
            }else{
                timeSlotArray.set((int)(normStartTime.get(j)*2),1);
            }
        }
        //getFreeSlots();
        getfreeTime();
        SelectedDateView.setText(freeSlotList.toString());
    }

    private void getfreeTime(){

        float startTime;
        float endTime;
        String startTimeString;
        String endTimeString;

        for(int i =0; i< timeSlotArray.size();i++){
            if(timeSlotArray.get(i) == 0){
                startTime = (float) (i*.5);
                startTimeString = convertToTimeSlot(startTime);
                int zeroCounter = 0;
                for(int j = i; j< timeSlotArray.size(); j++){
                    zeroCounter++;
                    if(timeSlotArray.get(j) == 1){
                        break;
                    }
                }
                endTime = (float) ((i+zeroCounter-1)*.5);
                endTimeString = convertToTimeSlot(endTime);
                i= i+zeroCounter-1;

                freeSlotList.add(startTimeString+" to "+endTimeString);
            }

        }

    }

    private String convertToTimeSlot(float decimalTime){
        String timeSlot;
        float fractionalPart = decimalTime%1;
        int integralPart = (int) (decimalTime - fractionalPart);

        if(fractionalPart == .5){
            timeSlot = integralPart+":30";
        }else{
            timeSlot = integralPart+":00";
        }

        if(integralPart < 10){
            timeSlot = "0"+timeSlot;
        }

        return timeSlot;

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
