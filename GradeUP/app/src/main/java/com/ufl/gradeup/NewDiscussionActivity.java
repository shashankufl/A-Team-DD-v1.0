package com.ufl.gradeup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NewDiscussionActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    Button createDiscussionButton;
    EditText newTopic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_discussion);
        createDiscussionButton= (Button) findViewById(R.id.gotoPublicDiscussion);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        newTopic= (EditText) findViewById(R.id.newDiscussionTopic);
        createDiscussionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDiscussionTopic=newTopic.getText().toString();
                ParseObject discussion= new ParseObject("Discussion");
                ParseACL acl= new ParseACL(ParseUser.getCurrentUser());
                acl.setPublicWriteAccess(true);
                acl.setPublicReadAccess(true);
                discussion.setACL(acl);
                discussion.put("Topic", newDiscussionTopic);
                discussion.put("Thread", "");
                discussion.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Toast.makeText(getApplicationContext(),"Discussion Post Created",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(NewDiscussionActivity.this,
                                    PublicDiscussionActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_discussion, menu);
        return true;
    }
}
