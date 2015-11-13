package com.ufl.gradeup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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
            sDate = "" + year + "-" + (month + 1) + "-" + day;
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
        final ViewScheduleCustomAdapter adapter = new ViewScheduleCustomAdapter<String>(schedule,this);

        checkschbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
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
                                        String schName = "" + object.getString("Subject") + "\n" +
                                                "From " + object.getString("Start_time") +
                                                " To " + object.getString("End_time");
                                        schedule.add(schName);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });

        //ScheduleListView.setItemsCanFocus(false);
        ScheduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Don't touch me", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(ViewScheduleActivity.this, UserProfileActivity.class);
//                startActivity(intent);
            }
        });
        ScheduleListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        ScheduleListView.setAdapter(adapter);
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
