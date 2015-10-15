package com.ufl.gradeup;
import android.graphics.Color;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.Touch;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    public static final int PICTURE_REQUEST = 20;
    Button registerButton;
    EditText registerEmail;
    EditText registerPassword;
    EditText registerName;
    EditText registerUniversity;
    byte[] ProfilePic;
	EditText confirmPassword;
    EditText registerFieldOfStudy;
    String regexEmail = "[a-zA-Z0-9._]+@[a-zA-Z0-9._]+\\.[A-Za-z]{2,6}";
    String regexName = "[a-zA-Z\\s]{2,32}";
    String regexPassword = "[a-zA-Z0-9^a-zA-Z0-9]{8,20}";
    String regexFieldOfStudy = "[a-zA-Z-.\\s]{8,32}";
    ParseFile pictureFile;

    private android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        registerName = (EditText)findViewById(R.id.nameTxt);
        registerEmail = (EditText)findViewById(R.id.emailTxt);
        registerPassword = (EditText) findViewById(R.id.passwordTxt);
        registerButton = (Button) findViewById(R.id.registerBtn);
        confirmPassword = (EditText) findViewById(R.id.confirmPasswordTxt);
        registerFieldOfStudy = (EditText) findViewById(R.id.studyFieldTxt);
        registerUniversity = (EditText) findViewById(R.id.universityTxt);

        registerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                Pattern p = Pattern.compile(regexName);
                Matcher m = p.matcher(registerName.getText());
                if (!m.matches()) {
                    registerName.setError("Name has to be between 2 and 32 characters");
                    registerButton.setEnabled(false);
                } else {
                    registerButton.setEnabled(true);
                }
            }
        });

        registerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                Pattern p = Pattern.compile(regexEmail);
                Matcher m = p.matcher(registerEmail.getText());
                if (!m.matches()) {

                    registerEmail.setError("Invalid email");
                    registerButton.setEnabled(false);
                } else {
                    registerButton.setEnabled(true);
                }
            }
        });

        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern p = Pattern.compile(regexPassword);
                Matcher m = p.matcher(registerPassword.getText());
                if (!m.matches()) {

                    registerPassword.setError("Password should be 8 to 20 characters in length");
                    registerButton.setEnabled(false);
                } else {
                    registerButton.setEnabled(true);
                }
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!registerPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                    confirmPassword.setError("Passwords do not match");
                    registerButton.setEnabled(false);
                } else {
                    registerButton.setEnabled(true);
                }
            }
        });
        registerFieldOfStudy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern p = Pattern.compile(regexFieldOfStudy);
                Matcher m = p.matcher(registerFieldOfStudy.getText());
                if (!m.matches()) {
                    registerFieldOfStudy.setError("Name has to be between 8 and 32 characters");
                    registerButton.setEnabled(false);
                } else {
                    registerButton.setEnabled(true);
                }
            }
        });

        // Sign up Button Click Listener
        registerButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                String emailTxt = registerEmail.getText().toString();
                String passwordTxt = registerPassword.getText().toString();
                String nameTxt = registerName.getText().toString();
                String universityTxt = registerUniversity.getText().toString();
                String fieldOfStudy = registerFieldOfStudy.getText().toString();
                // Force user to fill up the form
                if (emailTxt.equals("") && passwordTxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();

                } else {
                    // Save new user data into Parse.com Data Storage
                    ParseUser user = new ParseUser();

                    user.setUsername(emailTxt);
                    user.setPassword(passwordTxt);
                    user.setEmail(emailTxt);
                    user.put("Name", nameTxt);
                    user.put("University",universityTxt);
                    user.put("StudyField",fieldOfStudy);
                    user.put("ProfilePic", pictureFile);

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Show a simple Toast message upon successful registration
                                Intent intent = new Intent(RegisterActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),
                                        "Successfully Signed up, please try logging in now.",
                                        Toast.LENGTH_LONG).show();
                            } else {

                                Toast.makeText(getApplicationContext(),
                                        "Sign up Error"+e, Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                }

            }
        });

    }
    //To select profile pic from Gallery
    public void onSelectPicClicked(View v){
        Intent PicturePickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureSource =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureSourcePath = pictureSource.getPath();
        Uri uri = Uri.parse(pictureSourcePath);
        PicturePickerIntent.setDataAndType(uri, "image/*");
        startActivityForResult(PicturePickerIntent, PICTURE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICTURE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            //Picture address from phone storage
            Uri pictureUri = data.getData();
            try {
                String picName = registerName.getText().toString().replaceAll("\\s","")+".png";
                Bitmap PictureBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),pictureUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                PictureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                ProfilePic = stream.toByteArray();
                pictureFile = new ParseFile(picName, ProfilePic);  //registerName.getText().toString().trim()+
                pictureFile.saveInBackground();
                //Progress status for image upload
                final ProgressDialog pictureUploadProgress = new ProgressDialog(this);
                pictureUploadProgress.setTitle("Uploading Picture...");
                pictureUploadProgress.show();
                pictureFile.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        // If successful add file to user and signUpInBackground
                        if(null == e){
                            pictureUploadProgress.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "Picture Uploaded", Toast.LENGTH_SHORT)
                                    .show();
                            //savetoParse();
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    e+"", Toast.LENGTH_LONG)
                                    .show();
                        }

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,"Unable to open an image", Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
