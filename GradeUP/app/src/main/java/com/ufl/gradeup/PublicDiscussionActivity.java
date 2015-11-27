package com.ufl.gradeup;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublicDiscussionActivity extends AppCompatActivity{
    private android.support.v7.widget.Toolbar toolbar;
    ExpandableListView expandableListView;
    Button postCommentButton;
    List<String> headers= new ArrayList<>();
    List<String> L1= new ArrayList<>();
    List<String> L2= new ArrayList<>();
    List<String> L3= new ArrayList<>();
    HashMap<String,List<String>> comments= new HashMap<String,List<String>>();
    final PublicDiscussionAdapter adapter= new PublicDiscussionAdapter(this,headers,comments);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_discussion);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        expandableListView= (ExpandableListView)findViewById(R.id.publicDiscussionListView);
        postCommentButton= (Button) findViewById(R.id.postCommentButton);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Discussion");
        query.findInBackground(new FindCallback<ParseObject>(){
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        if(!headers.contains(object.getString("Topic"))){
                            headers.add(object.getString("Topic"));
                            List<String> temp=new ArrayList<String>();
                            temp.add(object.getString("Thread"));
                            comments.put(object.getString("Topic"),temp);
                        }
                    }
                    adapter.notifyDataSetChanged();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Error"+e,Toast.LENGTH_LONG).show();
                }
            }
        });
        expandableListView.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newDiscussion);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublicDiscussionActivity.this,
                        NewDiscussionActivity.class);
                startActivity(intent);
            }
        });
    }
    public void appendTheComment(final int groupPosition,String latestComment){
        List<String> listOfComments=comments.get(headers.get(groupPosition));
        String updatedComments=listOfComments.get(0);
        updatedComments+= "\n"+latestComment;
        final String commentString=updatedComments;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Discussion");
        query.whereEqualTo("Topic", headers.get(groupPosition));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                object.put("Thread",commentString);
                object.saveInBackground();
            }
        });
        listOfComments.set(0, updatedComments);
        comments.put(headers.get(groupPosition),listOfComments);
        final PublicDiscussionAdapter adapter= new PublicDiscussionAdapter(this,headers,comments);
        expandableListView.setAdapter(adapter);
    }

}
