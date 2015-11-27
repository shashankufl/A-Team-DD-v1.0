package com.ufl.gradeup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewScheduleActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    CalendarView mycalendar;
    String schName = null;
    int i=0;
    public static String sDate = "",schDate;
    ListView ScheduleListView;
    ArrayList<String> schedule = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        sDate = sdf.format(new Date());
        ScheduleListView = (ListView) findViewById(R.id.scheduleListView);
        ParseUser currentUser;
        currentUser = ParseUser.getCurrentUser();
        final String userEmail = currentUser.getString("username");
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,schedule);

        {
            // Retrieve user data from Parse.com Data Storage
            schedule.clear();//to clear contents of arraylist schedule
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
            query.whereEqualTo("User_ID", userEmail);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> nameList, ParseException e) {
                    if (e == null) {
                        i=0;
                        for (ParseObject object : nameList) {
                            if (new String(object.getString("Date")).equals(sDate)) {
                                i=1;
                                schName = "" + object.getString("Subject") + "\n" +
                                        "" + object.getString("Start_time") +
                                        "-" + object.getString("End_time");
                                schedule.add(schName);
                            }
                        }
                        if (i == 0) {
                            Toast.makeText(getBaseContext(), "No records found for " + sDate, Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });

        }

        mycalendar = (CalendarView) findViewById(R.id.calendarDisplay);
        mycalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
         @Override
         public void onSelectedDayChange(CalendarView view,int year,int month,int day){
             if(day/10 == 0){
                 sDate = "" + year + "-" + (month + 1) + "-0" + day;
             }
             else{
                 sDate = "" + year + "-" + (month + 1) + "-" + day;
             }
             {
                 // Retrieve user data from Parse.com Data Storage
                 schedule.clear();//to clear contents of arraylist schedule
                 ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
                 query.whereEqualTo("User_ID", userEmail);
                 query.findInBackground(new FindCallback<ParseObject>() {
                     @Override
                     public void done(List<ParseObject> nameList, ParseException e) {
                         if (e == null) {
                             i=0;
                             for (ParseObject object : nameList) {
                                 if (new String(object.getString("Date")).equals(sDate)) {
                                     i=1;
                                     schName = "" + object.getString("Subject") + "\n" +
                                             "" + object.getString("Start_time") +
                                             "-" + object.getString("End_time");
                                     schedule.add(schName);
                                 }
                             }
                             if (i == 0) {
                                 Toast.makeText(getBaseContext(), "No records found for " + sDate, Toast.LENGTH_LONG).show();
                             }
                             adapter.notifyDataSetChanged();
                         }
                     }
                 });

             }
         }
        });

        ScheduleListView.setAdapter(adapter);;
        ScheduleListView.setClickable(true);
        ScheduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedInstance, subject, startTime, endTime;
                clickedInstance = (String) ScheduleListView.getItemAtPosition(position);
                subject = clickedInstance.split("\\\n")[0];
                startTime = clickedInstance.substring(clickedInstance.indexOf("\n") + 1, clickedInstance.indexOf("-"));
                endTime = clickedInstance.substring(clickedInstance.lastIndexOf("-") + 1);
                Bundle extras = new Bundle();
                extras.putString("start_date", sDate);
                extras.putString("Subject", subject);
                extras.putString("Start_time", startTime);
                extras.putString("End_time", endTime);
                Intent intent = new Intent(ViewScheduleActivity.this, UpdateUserScheduleActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_schedule, menu);
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
//    public void showDatePickerDialog(View v) {
//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(getSupportFragmentManager(), "datePicker");
//    }

}