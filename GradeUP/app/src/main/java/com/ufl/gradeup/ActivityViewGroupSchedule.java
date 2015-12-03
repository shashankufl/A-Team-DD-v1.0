package com.ufl.gradeup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityViewGroupSchedule extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    CalendarView myCalendar;
    String sDate,schName,groupName;
    int i=0;
    ListView ScheduleListView;
    ArrayList<String> schedule = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_view_group_schedule);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        Intent intentGet= getIntent();
        groupName = intentGet.getStringExtra("groupName");

        final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        sDate = sdf.format(new Date());
        ScheduleListView = (ListView) findViewById(R.id.scheduleList);
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,schedule);

        //Fetching data for current date
        {
            // Retrieve group data from Parse.com Data Storage
            schedule.clear();//to clear contents of arraylist schedule
            ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberSchedule");
            query.whereEqualTo("groupName", groupName);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> nameList, ParseException e) {
                    if (e == null) {
                        i=0;
                        for (ParseObject object : nameList) {
                            if (new String(object.getString("Date")).equals(sDate)) {
                                i=1;
                                schName = "" + object.getString("meetingTitle") + "\n" +
                                        "" + object.getString("meetingStartTime") +
                                        "-" + object.getString("meetingEndTime");
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

        myCalendar = (CalendarView) findViewById(R.id.calendarDisplay);
        myCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(CalendarView view,int year,int month,int day){
                if(day/10 == 0){
                    sDate = "" + year + "-" + (month + 1) + "-0" + day;
//                    sDate = "" + year + "-" + (month + 1) + "-" + day;
                }
                else{
                    sDate = "" + year + "-" + (month + 1) + "-" + day;
                }
                {
                    // Retrieve group data from Parse.com Data Storage
                    schedule.clear();//to clear contents of arraylist schedule
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberSchedule");
                    query.whereEqualTo("groupName", groupName);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> nameList, ParseException e) {
                            if (e == null) {
                                i=0;
                                for (ParseObject object : nameList) {
                                    if (new String(object.getString("Date")).equals(sDate)) {
                                        i=1;
                                        schName = "" + object.getString("meetingTitle") + "\n" +
                                                "" + object.getString("meetingStartTime") +
                                                "-" + object.getString("meetingEndTime");
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
        ScheduleListView.setAdapter(adapter);
        ScheduleListView.setClickable(true);
        ScheduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedInstance, subject, startTime, endTime;
                clickedInstance = (String) ScheduleListView.getItemAtPosition(position);
                subject = clickedInstance.split("\\\n")[0];
                startTime = clickedInstance.substring(clickedInstance.indexOf("\n") + 1, clickedInstance.indexOf("-"));
                endTime = clickedInstance.substring(clickedInstance.lastIndexOf("-") + 1);
                Bundle sendingExtras = new Bundle();
                sendingExtras.putString("groupName", groupName);
                sendingExtras.putString("start_date", sDate);
                sendingExtras.putString("meetingTitle", subject);
                sendingExtras.putString("Start_time", startTime);
                sendingExtras.putString("End_time", endTime);
                Intent intentSend = new Intent(ActivityViewGroupSchedule.this, UpdateGroupScheduleActivity.class);
                intentSend.putExtras(sendingExtras);
                startActivity(intentSend);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_view_group_schedule, menu);
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
