package com.ufl.gradeup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViewScheduleActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    public static TextView SelectedDateView;
    Button checkschbtn;
    String schName = null;
    public static String sDate = "",schDate;
    ListView ScheduleListView;
    ArrayList<String> schedule = new ArrayList<String>();

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
            if(day/10 == 0){
                sDate = "" + year + "-" + (month + 1) + "-0" + day;
            }
            else{
                sDate = "" + year + "-" + (month + 1) + "-" + day;
            }
            SelectedDateView.setText(sDate);
        }
    }
    //Date Picking and saving ends here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        checkschbtn = (Button) findViewById(R.id.checkschbtn);
        SelectedDateView = (TextView) findViewById(R.id.sdate);
        ScheduleListView = (ListView) findViewById(R.id.scheduleListView);
        ParseUser currentUser;
        currentUser = ParseUser.getCurrentUser();
        final String userEmail = currentUser.getString("username");
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,schedule);

        checkschbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                schedule.clear();//to clear contents of arraylist schedule
                if (sDate.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please provide a date for your schedule",
                            Toast.LENGTH_LONG).show();
                } else {
                    // Retrieve user data from Parse.com Data Storage
                    schedule.clear();//to clear contents of arraylist schedule
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
                    query.whereEqualTo("User_ID", userEmail);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> nameList, ParseException e) {
                            if (e == null) {
                                for (ParseObject object : nameList) {
                                    if (new String(object.getString("Date")).equals(sDate)) {
                                        schName = "" + object.getString("Subject") + "\n" +
                                                "" + object.getString("Start_time") +
                                                "-" + object.getString("End_time");
                                        schedule.add(schName);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                    if(ScheduleListView == null){
                        Toast.makeText(getBaseContext(),"No records found for " + sDate,Toast.LENGTH_LONG).show();
                    }
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
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}
