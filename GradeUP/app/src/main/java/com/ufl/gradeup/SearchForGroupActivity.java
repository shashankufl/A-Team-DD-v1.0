package com.ufl.gradeup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchForGroupActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar searchGrpToolbar;
    Button searchButton;
    EditText searchStringText;
    String groupName;
    TextView groupNameView;
    List<String> groupNamesList = new ArrayList<String>();
    ListView groupNamesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_group);
        searchGrpToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(searchGrpToolbar);
        final ArrayAdapter<String> adapter = new  ArrayAdapter<String>(SearchForGroupActivity.this, android.R.layout.simple_expandable_list_item_1, groupNamesList);
        searchButton = (Button) findViewById(R.id.searchForGroupButton);
        searchStringText = (EditText) findViewById(R.id.searchForGroupText);
        //groupNameView= (TextView) findViewById(R.id.groupNameTextView);
        groupNamesListView = (ListView)findViewById(R.id.groupListView);
        searchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                query.whereEqualTo("groupName", searchStringText.getText().toString());
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            for (ParseObject object : objects
                                    ) {

                                if (!groupNamesList.contains(object.getString("groupName"))) {
                                    groupNamesList.add(object.getString("groupName"));
                                }
                                groupName = object.getString("groupName");

                               // groupNameView.setText(groupName);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });


        groupNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchForGroupActivity.this,
                        GroupHomeActivity.class);
                intent.putExtra("groupName",groupName);
                startActivity(intent);
            }
        });

        groupNamesListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        groupNamesListView.setAdapter(adapter);
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
