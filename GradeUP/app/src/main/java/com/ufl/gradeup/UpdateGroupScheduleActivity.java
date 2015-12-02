package com.ufl.gradeup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UpdateGroupScheduleActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    String meetingName, oldMeetingName, oldStartDate, oldEndTime, oldStartTime,
            startDate, startTime, endTime, groupName, Agenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_group_schedule);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        Intent intentGet= getIntent();
        Bundle bundle = intentGet.getExtras();

        groupName = bundle.getString("groupName");
        oldStartDate = bundle.getString("start_date");
        startDate = oldStartDate;
        oldStartTime = bundle.getString("Start_time");
        startTime = oldStartTime;
        oldEndTime = bundle.getString("End_time");
        endTime = oldEndTime;
        oldMeetingName = bundle.getString("meetingTitle");
        meetingName = oldMeetingName;

        TextView meetingTitle = (TextView) findViewById(R.id.fetchMeetingTitle);
        meetingTitle.setText(meetingName);
        TextView meetingDate = (TextView) findViewById(R.id.fetchStartDate);
        meetingDate.setText(startDate);
        TextView meetingStartTime = (TextView) findViewById(R.id.fetchMeetingStartTime);
        meetingStartTime.setText(startTime);
        TextView meetingEndTime = (TextView) findViewById(R.id.fetchMeetingEndTime);
        meetingEndTime.setText("to " + endTime);
        final TextView meetingAgenda = (TextView) findViewById(R.id.fetchMeetingAgenda);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberSchedule");
        query.whereEqualTo("groupName", groupName);
        query.whereEqualTo("meetingTitle", meetingName);
        query.whereEqualTo("meetingEndTime", endTime);
        query.whereEqualTo("meetingStartTime", startTime);
        query.whereEqualTo("Date", startDate);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> nameList, ParseException e) {
                if (e == null) {
                    for (ParseObject object : nameList) {
                        meetingAgenda.setText(object.getString("Agenda"));
                        break;
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_group_schedule, menu);
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
