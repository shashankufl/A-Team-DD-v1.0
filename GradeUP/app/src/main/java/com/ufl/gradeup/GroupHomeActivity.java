package com.ufl.gradeup;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupHomeActivity extends AppCompatActivity {
    String append;
    private Menu joinMenu;
    private String joinOrCancel = "Join";
    private String groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_home);

        groupName = getIntent().getStringExtra("groupName");


        Toolbar groupProfileToolbar = (Toolbar) findViewById(R.id.groupHomeToolbar);
        setSupportActionBar(groupProfileToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_groupPic);
        collapsingToolbar.setTitle("Add Group Name Here");

        NavigationDrawerFragment navDrawerFrag = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drawer_fragment);
        navDrawerFrag.setUp(R.id.nav_drawer_fragment, (DrawerLayout) findViewById(R.id.group_drawer_layout), groupProfileToolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_home, menu);

        MenuItem approveRequestMenuItem = menu.findItem(R.id.approveRequestListItem);

        try {
            approveRequestMenuItem.setVisible(isAdmin());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        joinMenu = menu;
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

        if(id == R.id.joinGroupListItem)
        {
            if(joinOrCancel.equalsIgnoreCase("Join")) {
                item.setTitle("Cancel Request");
                String test = getIntent().getStringExtra("groupName");
                final ParseUser requestingUser = ParseUser.getCurrentUser();
                final String joinRequest = requestingUser.getUsername()+","+groupName;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                query.whereEqualTo("groupName", getIntent().getStringExtra("groupName"));
                query.whereEqualTo("isAdmin", 1);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        for (ParseObject object : list
                                ) {

                            object.addAllUnique("joinRequests", Arrays.asList(joinRequest));
                            object.saveInBackground();
//                           if(object.getString("joinRequests") == null){
//                            object.put("joinRequests",requestingUser.getUsername());
//                            object.saveInBackground();
//                        }else{
//                            append = object.getString("joinRequests")+","+requestingUser.getUsername();
//                            object.put("joinRequests",append);
//                            object.saveInBackground();
//                        }
                    }
                    }
                });

                joinOrCancel = "Cancel";
            }else if (joinOrCancel.equalsIgnoreCase("Cancel")){
                item.setTitle("Join Group");
                joinOrCancel = "Join";

                final ParseUser requestingUser = ParseUser.getCurrentUser();

               final String joinRequest = requestingUser.getUsername()+","+groupName;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                query.whereEqualTo("groupName", getIntent().getStringExtra("groupName"));
                query.whereEqualTo("isAdmin", 1);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        for (ParseObject object : list
                                ) {

                            object.removeAll("joinRequests", Arrays.asList(joinRequest));
                            object.saveInBackground();
                        }
                    }
                });
            }

            return true;
        }

        if(id == R.id.approveRequestListItem){


            Intent intent = new Intent(
                    GroupHomeActivity.this,
                    ApproveRequestActivity.class);
            intent.putExtra("groupName",groupName);
            startActivity(intent);


        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isAdmin() throws ParseException {

        final ParseUser requestingUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("groupName", getIntent().getStringExtra("groupName"));
        query.whereEqualTo("isAdmin", 1);
        query.whereEqualTo("userName", requestingUser.getUsername());
        if(query.count() == 1){
            return true;
        }
        return false;
    }
}
