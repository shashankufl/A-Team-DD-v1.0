package com.ufl.gradeup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class DeleteScheduleActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;

    String sName = null, sDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_schedule);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
//        final ViewScheduleCustomAdapter adapter = new ViewScheduleCustomAdapter<String>(schedule,this);
//
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
//        query.whereEqualTo("Name", sName);
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> nameList, ParseException e) {
//                if (e == null) {
//                    for (ParseObject object : nameList) {
//                        if (new String(object.getString("Date")).equals(sDate)) {
//
//                            schedule.delete(schName);
//                        }
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete_schedule, menu);
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
}
