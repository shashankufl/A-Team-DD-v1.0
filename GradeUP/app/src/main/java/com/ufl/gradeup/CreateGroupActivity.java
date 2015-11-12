package com.ufl.gradeup;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;



public class CreateGroupActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;

    Button createGroupButton;
    String userName;
    ParseUser currentUser;
    EditText groupName;
    EditText addGroupMembers;
    ListView groupMembersListView;
    List<String> groupMemebers = new ArrayList<String>();
    List<String> groupMemebersUsername = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        groupName = (EditText)findViewById(R.id.groupNameTxt);
        addGroupMembers = (EditText)findViewById(R.id.addGroupMemberTxt);
        groupMembersListView = (ListView)findViewById(R.id.memberListView);
        createGroupButton= (Button) findViewById(R.id.gotoGroupHome);
        currentUser = ParseUser.getCurrentUser();
        userName = currentUser.getUsername();

        final CreateGroupCustomAdapter adapter =  new CreateGroupCustomAdapter(groupMemebers,this);

        addGroupMembers.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (addGroupMembers.getRight() - addGroupMembers.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("username", addGroupMembers.getText().toString());
                        query.findInBackground(new FindCallback<ParseUser>() {
                            public void done(List<ParseUser> objects, ParseException e) {
                                if (e == null) {
                                    for (ParseUser object : objects
                                            ) {
                                        Toast.makeText(getApplicationContext(),
                                                object.getString("Name"),
                                                Toast.LENGTH_LONG).show();
                                        if (!groupMemebersUsername.contains(object.getUsername())) {
                                            groupMemebers.add(object.getString("Name"));
                                            groupMemebersUsername.add(object.getUsername());
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
//                                    Toast.makeText(getApplicationContext(),
//                                            objects.getString(),
//                                            Toast.LENGTH_LONG).show();
                                } else {
                                    // Something went wrong.
                                }
                            }
                        });
                        return true;
                    }
                }
                return false;
            }
        });
//        adapter = new ArrayAdapter<String>(CreateGroupActivity.this, android.R.layout.simple_expandable_list_item_1, groupMemebers);
        groupMembersListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });

        groupMembersListView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
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
        if(id == R.id.gotoGroupHome){

            String nameOfGroup= groupName.getText().toString();
            String usernameOfUser= userName;
            ParseObject group = new ParseObject("Groups");
            group.put("groupName", nameOfGroup);
            group.put("userName", usernameOfUser);
            group.put("isAdmin",1);
            ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
            acl.setPublicWriteAccess(true);
            acl.setPublicReadAccess(true);
            group.setACL(acl);
            group.saveInBackground();

            for(int i=0; i < groupMembersListView.getCount(); i++)
            {
                group = new ParseObject("Groups");
                group.put("groupName", nameOfGroup);
                group.put("userName", groupMemebersUsername.get(i));
                group.put("isAdmin",0);
                group.saveInBackground();
            }
            Toast.makeText(
                    getApplicationContext(),
                    "Group created successfully" ,
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CreateGroupActivity.this,
                    GroupHomeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
