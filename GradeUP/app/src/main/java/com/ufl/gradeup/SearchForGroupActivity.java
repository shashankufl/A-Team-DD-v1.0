package com.ufl.gradeup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchForGroupActivity extends AppCompatActivity {

    Button searchButton;
    EditText searchStringText;
    String groupNames;
    TextView groupNameView;
//    List<String> groupNames = new ArrayList<String>();
//    ListView groupNamesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_group);
        /*final CreateGroupCustomAdapter adapter =  new CreateGroupCustomAdapter(groupNames,this);*/
        searchButton = (Button) findViewById(R.id.searchForGroupButton);
        searchStringText = (EditText) findViewById(R.id.searchForGroupText);
//        groupNamesListView = (ListView)findViewById(R.id.groupListView);
        searchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                query.whereEqualTo("groupName", searchStringText.getText().toString());
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            for (ParseObject object : objects
                                    ) {
                                Toast.makeText(getApplicationContext(),
                                        object.getString("userName"),
                                        Toast.LENGTH_LONG).show();
//                                if (!groupNames.contains(object.getString("groupName"))) {
//                                    groupNames.add(object.getString("groupName"));
//                                }
                                groupNames= object.getString("groupName");
                                groupNameView= (TextView) findViewById(R.id.groupNameTextView);
                                groupNameView.setText(groupNames);
                            }
//                            adapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });

//        groupNamesListView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                return false;
//            }
//        });
//        groupNamesListView.setAdapter(adapter);
//        groupNameView.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                Intent intent = new Intent(SearchForGroupActivity.this,
//                        GroupHomeActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    public void gotoGroupHome(View v){

        Intent intent = new Intent(SearchForGroupActivity.this,
                        GroupHomeActivity.class);
                startActivity(intent);
    }
}
