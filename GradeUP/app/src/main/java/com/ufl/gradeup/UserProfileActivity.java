package com.ufl.gradeup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    byte[] ProfilePic;
    public static final int PICTURE_REQUEST = 20;
    ParseFile pictureFile;
    ImageView userProfilePic;
    String name;
    String picName = "profilePic.png";
    ParseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();

        //ParseUser user = new ParseUser();
         currentUser = ParseUser.getCurrentUser();
        String objectId = currentUser.getObjectId();
        name = currentUser.getString("Name");
        TextView universityTextView = (TextView) findViewById(R.id.university);
        TextView emailTextView = (TextView) findViewById(R.id.email);
        TextView fieldOfStudyTextView = (TextView) findViewById(R.id.studyField);
        universityTextView.setText(currentUser.getString("University"));
        emailTextView.setText(currentUser.getEmail());
        fieldOfStudyTextView.setText(currentUser.getString("StudyField")+"Department");
        userProfilePic = (ImageView) findViewById(R.id.ProfileImage);

        ParseFile profilePictureFile = currentUser.getParseFile("ProfilePic");
        final ProgressDialog pictureUploadProgress = new ProgressDialog(this);
        pictureUploadProgress.setTitle("Loading Profile...");
        pictureUploadProgress.show();
        profilePictureFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {

                if (e == null) {
                    Bitmap profilePicBmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//                    userProfilePic = (ImageView) findViewById(R.id.ProfileImage);
                    userProfilePic.setImageBitmap(profilePicBmp);
                    ImageView navProfilePic = (ImageView) findViewById(R.id.navImage);
                    navProfilePic.setImageBitmap(profilePicBmp);
                    TextView navTitle = (TextView) findViewById(R.id.navTitleText);
                    navTitle.setText(name);
                    pictureUploadProgress.dismiss();
                } else {
                    //userProfilePic.setImageBitmap(R.mipmap.xyz);
                }
            }
        });

        //debugger;
        Toolbar profileToolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(profileToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_profilePic);
        collapsingToolbar.setTitle(name);

        //Setting RecyclerView for Today's Schedule
        RecyclerView todaySchedule = (RecyclerView)findViewById(R.id.todaySchedule);
        todaySchedule.setHasFixedSize(true);

        LinearLayoutManager todayScheduleManager = new LinearLayoutManager(this);
        todaySchedule.setLayoutManager(todayScheduleManager);


        NavigationDrawerFragment navDrawerFrag = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drawer_fragment);
        navDrawerFrag.setUp(R.id.nav_drawer_fragment, (DrawerLayout) findViewById(R.id.drawer_layout), profileToolbar);

//        userProfilePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onProfilePicClicked(v);
//            }
//        });

//        userProfilePic.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                onProfilePicClicked();
//                return false;
//            }
//        });
    }

    //To select profile pic from Gallery
    public void onProfilePicClicked(){
        Intent PicturePickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureSource = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
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
                if(name.trim().length()>0){
                    picName = name.replaceAll("\\s", "")+".png";
                }
                final Bitmap PictureBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),pictureUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                PictureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                ProfilePic = stream.toByteArray();
                pictureFile = new ParseFile(picName, ProfilePic);  //registerName.getText().toString().trim()+
                pictureFile.saveInBackground();
                //Progress status for image upload
                final ProgressDialog pictureUploadProgress = new ProgressDialog(this);
                pictureUploadProgress.setTitle("Uploading Picture...");
                pictureUploadProgress.show();

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.getInBackground(currentUser.getObjectId(), new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(e == null) {
                            pictureUploadProgress.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "Picture Uploaded", Toast.LENGTH_SHORT)
                                    .show();
                            currentUser.put("ProfilePic", pictureFile);
                            userProfilePic.setImageBitmap(PictureBitmap);
                            ImageView navProfilePic = (ImageView) findViewById(R.id.navImage);
                            navProfilePic.setImageBitmap(PictureBitmap);
                            currentUser.saveInBackground();


                        }else{
                            Toast.makeText(getApplicationContext(),
                                    e+"", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
                //                pictureFile.saveInBackground(new SaveCallback() {
//                    public void done(ParseException e) {
//                        // If successful add file to user and signUpInBackground
//                        if(null == e){
//                            pictureUploadProgress.dismiss();
//                            Toast.makeText(getApplicationContext(),
//                                    "Picture Uploaded", Toast.LENGTH_SHORT)
//                                    .show();
//                            currentUser.put("TestPic", pictureFile);
//                            //savetoParse();
//                        }else{
//                            Toast.makeText(getApplicationContext(),
//                                    e+"", Toast.LENGTH_LONG)
//                                    .show();
//                        }
//
//                    }
//                });

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,"Unable to open an image", Toast.LENGTH_LONG).show();
            }

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.updateProfile) {
            Intent intent = new Intent(UserProfileActivity.this,
                    UpdateProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.updateProfilePicture){
            onProfilePicClicked();
            return true;
        }
        if (id == R.id.logOut){

            ParseUser.logOut();
            finish();
        }
        if (id == R.id.creategroupMenuItem) {
            Intent intent = new Intent(UserProfileActivity.this,
                    CreateGroupActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.searchForGroups) {
            Intent intent = new Intent(UserProfileActivity.this,
                    SearchForGroupActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
