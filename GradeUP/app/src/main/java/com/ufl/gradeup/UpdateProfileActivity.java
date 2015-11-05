package com.ufl.gradeup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileActivity extends AppCompatActivity {

    Button updateButton;
    EditText updateEmail;
    EditText updateName;
    EditText updateUniversity;
    EditText updatePhoneNumber;
    EditText updateFieldOfStudy;
    ParseUser currentUser;
    String regexEmail = "[a-zA-Z0-9._]+@[a-zA-Z0-9._]+\\.[A-Za-z]{2,6}";
    String regexName = "[a-zA-Z\\s]{2,32}";
    String regexPassword = "[a-zA-Z0-9^a-zA-Z0-9]{8,20}";
    String regexFieldOfStudy = "[a-zA-Z-.\\s]{2,32}";

    String emailTxt ;
    String nameTxt ;
    String universityTxt;
    String fieldOfStudy;
    String phoneTxt;
    private android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        updateName = (EditText)findViewById(R.id.updateNameTxt);
        updateEmail = (EditText)findViewById(R.id.updateEmailTxt);
        updateButton= (Button) findViewById(R.id.updateBtn);
        updateUniversity = (EditText) findViewById(R.id.updateUniversityTxt);
        updatePhoneNumber = (EditText) findViewById(R.id.updatePhoneTxt);
        updateFieldOfStudy = (EditText) findViewById(R.id.updateStudyFieldTxt);
         currentUser = ParseUser.getCurrentUser();

        updateName.setText(currentUser.getString("Name"));
        updateUniversity.setText(currentUser.getString("University"));
        updateEmail.setText(currentUser.getString("email"));
        updatePhoneNumber.setText(currentUser.getString("phone"));
        updateFieldOfStudy.setText(currentUser.getString("StudyField"));
        final String PasswordTxt = currentUser.getString("password");

        updateName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                Pattern p = Pattern.compile(regexName);
                Matcher m = p.matcher(updateName.getText());
                if (!m.matches()) {
                    updateName.setError("Name has to be between 2 and 32 characters");
                    updateButton.setEnabled(false);
                } else {
                    updateButton.setEnabled(true);
                }
            }
        });

        updateEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern p = Pattern.compile(regexEmail);
                Matcher m = p.matcher(updateEmail.getText());
                if (!m.matches()) {

                    updateEmail.setError("Invalid email");
                    updateButton.setEnabled(false);
                } else {
                    updateButton.setEnabled(true);
                }
            }
        });


        updateFieldOfStudy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern p = Pattern.compile(regexFieldOfStudy);
                Matcher m = p.matcher(updateFieldOfStudy.getText());
                if (!m.matches()) {
                    updateFieldOfStudy.setError("Name has to be between 8 and 32 characters");
                    updateButton.setEnabled(false);
                } else {
                    updateButton.setEnabled(true);
                }
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 emailTxt = updateEmail.getText().toString();
                 nameTxt = updateName.getText().toString();
                 universityTxt = updateUniversity.getText().toString();
                 fieldOfStudy = updateFieldOfStudy.getText().toString();
                 phoneTxt = updatePhoneNumber.getText().toString();

                if (nameTxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Name Field is Required",
                            Toast.LENGTH_LONG).show();

                } else {

                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.getInBackground(currentUser.getObjectId(), new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if(e == null) {
                                parseUser.setUsername(emailTxt);
                               // parseUser.setPassword(PasswordTxt);
                                parseUser.setEmail(emailTxt);
                                parseUser.put("Name", nameTxt);
                                parseUser.put("University", universityTxt);
                                parseUser.put("StudyField", fieldOfStudy);
                                parseUser.put("phone", phoneTxt);
                                parseUser.saveInBackground();
                                Toast.makeText(getApplicationContext(),
                                        "Profile Updated",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(UpdateProfileActivity.this,
                                        UserProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),
                                        "Error",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });







                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_profile, menu);
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
