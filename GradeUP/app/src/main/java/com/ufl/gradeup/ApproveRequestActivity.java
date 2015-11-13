package com.ufl.gradeup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ApproveRequestActivity extends AppCompatActivity {

    ArrayList<String> pendingRequestsList = new ArrayList<String>();
    ListView pendingRequestListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_request);
    String test = getIntent().getStringExtra("memberName");
        pendingRequestListView = (ListView)findViewById(R.id.pendingRequestListView);
        //final ArrayAdapter<String> adapter = new  ArrayAdapter<String>(ApproveRequestActivity.this, android.R.layout.simple_expandable_list_item_1, pendingRequestsList);
        final ApproveRequestCustomAdapter adapter =  new ApproveRequestCustomAdapter(pendingRequestsList,this, getIntent().getStringExtra("groupName"), getIntent().getStringExtra("memberName"));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("groupName", getIntent().getStringExtra("groupName"));
        query.whereEqualTo("isAdmin", 1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject object : list
                        ) {
                    pendingRequestsList.addAll((ArrayList<String>) object.get("joinRequests")) ;
                    adapter.notifyDataSetChanged();
                }

            }
        });
        pendingRequestListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        pendingRequestListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_approve_request, menu);
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
