package com.ufl.gradeup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.RequestPasswordResetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
/**
 * Created by shweta on 11/16/2015.
 */
public class ForgotPassActivity extends AppCompatActivity{
    EditText email;
    Button retrieveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass);

        email = (EditText)findViewById(R.id.emailText);
        retrieveButton = (Button) findViewById(R.id.RetrieveBtn);
        retrieveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                String emailTxt = email.getText().toString();

                // Send data to Parse.com for verification

                ParseUser.requestPasswordResetInBackground(emailTxt,
                        new RequestPasswordResetCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "An email was successfully sent with reset instructions.",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Invalid credentials: Incorrect Email address",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        TextView retryLogin = (TextView)findViewById(R.id.RetryLogin);
        retryLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotPassActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
