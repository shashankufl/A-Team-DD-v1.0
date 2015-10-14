package com.example.shashank.logintest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;


public class Welcome extends Activity {

    // Declare Variable
    Button logout;
    Button postbtn;
    EditText txtdiscuss = null;
    String threadtxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.welcome);

        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();

        // Convert currentUser into String
        String struser = currentUser.getUsername().toString();

        // Locate TextView in welcome.xml
        TextView txtuser = (TextView) findViewById(R.id.txtuser);

        // Set the currentUser String into TextView
        txtuser.setText("You are logged in as " + struser);

        //discussion.saveInBackground();
        txtdiscuss = (EditText) findViewById(R.id.txtdiscuss);
        // Post Button Click Listener
        postbtn = (Button) findViewById(R.id.postbtn);
        postbtn.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                threadtxt = txtdiscuss.getText().toString();

                if (threadtxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter text",
                            Toast.LENGTH_LONG).show();

                } else {
                    // Save new user data into Parse.com Data Storage
                    ParseObject discussion = new ParseObject("Discussion");
                    discussion.put("Thread", threadtxt);
                    discussion.saveInBackground();
                }

            }
        });

        // Locate Button in welcome.xml
        logout = (Button) findViewById(R.id.logout);

        // Logout Button Click Listener
        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                finish();
            }
        });
    }
}