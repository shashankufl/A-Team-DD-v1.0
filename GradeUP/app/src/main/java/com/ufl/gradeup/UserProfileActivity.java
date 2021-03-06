package com.ufl.gradeup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class UserProfileActivity extends AppCompatActivity {

    byte[] ProfilePic;
    public static final int PICTURE_REQUEST = 20;
    ParseFile pictureFile;
    ImageView userProfilePic;
    String name;
    String picName = "profilePic.png";
    ParseUser currentUser;
    List<String> groupNamesList = new ArrayList<String>();
    ArrayList<String> todayScheduleTitleList = new ArrayList<String>();
    ArrayList<String> todayScheduleTimeList = new ArrayList<String>();
    SwipeRefreshLayout swipeLayout;

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
        TextView phoneTextView = (TextView) findViewById(R.id.phone);
        universityTextView.setText(currentUser.getString("University"));
        emailTextView.setText(currentUser.getEmail());

        fieldOfStudyTextView.setText(currentUser.getString("StudyField") + "Department");
        if (currentUser.getString("phone") != "") {
            phoneTextView.setText(currentUser.getString("phone"));
        }
        userProfilePic = (ImageView) findViewById(R.id.ProfileImage);

        ParseFile profilePictureFile = currentUser.getParseFile("ProfilePic");
        TextView navTitle = (TextView) findViewById(R.id.navTitleText);
        navTitle.setText(name);
        if(profilePictureFile!=null){
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
                        pictureUploadProgress.dismiss();
                    } else {
                        //userProfilePic.setImageBitmap(R.mipmap.xyz);
                    }
                }
            });
        }


        try {
            getGroups();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getTodaySchedule();


        Toolbar profileToolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(profileToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_profilePic);
        collapsingToolbar.setTitle(name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this,
                        CreateGroupActivity.class);
                startActivity(intent);
            }
        });


        NavigationDrawerFragment navDrawerFrag = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drawer_fragment);
        navDrawerFrag.setUp(R.id.nav_drawer_fragment, (DrawerLayout) findViewById(R.id.drawer_layout), profileToolbar);

//        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//        swipeLayout.setOnRefreshListener(this);
//        swipeLayout.setColorSchemeColors(R.color.primaryColor);


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

//    @Override
//    public void onRefresh() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                swipeLayout.setRefreshing(false);
//            }
//        }, 3000);
//    }

    // //Retrieve groups from Parse
    public void getGroups() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("userName", currentUser.getUsername());
        final ProgressDialog groupLoadProgress = new ProgressDialog(this);
        groupLoadProgress.setTitle("Loading Groups...");
        groupLoadProgress.show();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects
                            ) {
//                        Toast.makeText(getApplicationContext(),
//                                object.getString("userName"),
//                                Toast.LENGTH_LONG).show();
                        if (!groupNamesList.contains(object.getString("groupName"))) {
                            groupNamesList.add(object.getString("groupName"));
                        }
                    }
                    bindGroups();
                    groupLoadProgress.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(),
                            e + "", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    //Create dynamic UI from groups
    public void bindGroups() {
        LinearLayout groupLayout = (LinearLayout) findViewById(R.id.groupsCard);
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        int[] icons = {R.mipmap.group1_icon,R.mipmap.group2_icon,R.mipmap.group3_icon,R.mipmap.group4_icon,R.mipmap.group5_icon};
        if (groupNamesList.size() == 0) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            textView.setTypeface(textView.getTypeface(), Typeface.ITALIC);
            textView.setText("Nothing to Show...");
            if (Build.VERSION.SDK_INT < 23) {
                textView.setTextAppearance(this, android.R.style.TextAppearance_Material_Small);
            } else {
                textView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
            }
            groupLayout.addView(textView);
        }else{
            for (int i = 0; i < groupNamesList.size(); i++) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
                textView.setText("   " + groupNamesList.get(i));
                textView.setFocusable(true);
                textView.setClickable(true);
                textView.setBackgroundResource(backgroundResource);
                textView.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        String groupNameSelected = ((TextView)v).getText().toString();
                        groupNameSelected =groupNameSelected.replaceAll("^\\s+", "");
                        Intent intent = new Intent(UserProfileActivity.this,
                                GroupHomeActivity.class);
                        intent.putExtra("groupName",groupNameSelected);
                        startActivity(intent);

                    }
                }
            );
                textView.setPadding(0, 20, 0, 20);
                textView.setCompoundDrawablesWithIntrinsicBounds(icons[new Random().nextInt(icons.length)], 0, 0, 0);
                if (Build.VERSION.SDK_INT < 23) {
                    textView.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
                } else {
                    textView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Medium);
                }
                groupLayout.addView(textView);
                if (i < groupNamesList.size() - 1) {
                    View view = new View(this);
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1);
                    params.setMargins(0, 15, 0, 15);
                    view.setLayoutParams(params);
                    view.setBackgroundColor(Color.rgb(169, 169, 169));
                    groupLayout.addView(view);
                }
            }

        }


    }

    //Create dynamic UI from today's schedule
    public void bindTodaySchedule() {
        LinearLayout scheduleLayout = (LinearLayout) findViewById(R.id.todayScheduleCard);
        if (todayScheduleTitleList.size() == 0) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            textView.setTypeface(textView.getTypeface(), Typeface.ITALIC);
            textView.setText("Nothing to Show...");
            if (Build.VERSION.SDK_INT < 23) {
                textView.setTextAppearance(this, android.R.style.TextAppearance_Material_Small);
            } else {
                textView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
            }
            scheduleLayout.addView(textView);
        } else {
            for (int i = 0; i < todayScheduleTitleList.size(); i++) {
                TextView titletextView = new TextView(this);
                TextView timeTextView = new TextView(this);
                titletextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
                titletextView.setText("  " + todayScheduleTitleList.get(i));
                titletextView.setCompoundDrawablePadding(R.mipmap.todaysch_icon);
                titletextView.setFocusable(true);
                titletextView.setClickable(true);

                timeTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
                timeTextView.setText("  " + todayScheduleTimeList.get(i));
                timeTextView.setFocusable(true);
                timeTextView.setClickable(true);


                if (Build.VERSION.SDK_INT < 23) {
                    titletextView.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
                    timeTextView.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
                } else {
                    titletextView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Medium);
                    timeTextView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
                }

                scheduleLayout.addView(titletextView);
                scheduleLayout.addView(timeTextView);

                if (i < todayScheduleTitleList.size() - 1) {
                    View view = new View(this);
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1);
                    params.setMargins(0, 15, 0, 15);
                    view.setLayoutParams(params);
                    view.setBackgroundColor(Color.rgb(169, 169, 169));
                    scheduleLayout.addView(view);
                }
            }
        }


    }

    //Retrieve today's Schedule from Parse
    public void getTodaySchedule() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
        query.whereEqualTo("User_ID", currentUser.getUsername());
        final ProgressDialog scheduleLoadProgress = new ProgressDialog(this);
        scheduleLoadProgress.setTitle("Loading Schedule...");
        scheduleLoadProgress.show();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String today = simpleDateFormat.format(new Date());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> nameList, ParseException e) {
                if (e == null) {
                    for (ParseObject object : nameList) {
                        if (new String(object.getString("Date")).equals(today)) {
                            String schName = "" + object.getString("Subject");
                            String schTime = object.getString("Start_time") +
                                    " to " + object.getString("End_time");
                            todayScheduleTitleList.add(schName);
                            todayScheduleTimeList.add(schTime);
                        }
                    }
                    scheduleLoadProgress.dismiss();
                    bindTodaySchedule();

                } else {
                    Log.d("error", "Error: " + e.getMessage());
                }
            }
        });
    }

    //To select profile pic from Gallery
    public void onProfilePicClicked() {
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
        if (requestCode == PICTURE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //Picture address from phone storage
            Uri pictureUri = data.getData();
            try {
                if (name.trim().length() > 0) {
                    picName = name.replaceAll("\\s", "") + ".png";
                }
                final Bitmap PictureBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), pictureUri);
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
                        if (e == null) {
                            pictureUploadProgress.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "Picture Uploaded", Toast.LENGTH_SHORT)
                                    .show();
                            currentUser.put("ProfilePic", pictureFile);
                            userProfilePic.setImageBitmap(PictureBitmap);
                            ImageView navProfilePic = (ImageView) findViewById(R.id.navImage);
                            navProfilePic.setImageBitmap(PictureBitmap);
                            currentUser.saveInBackground();


                        } else {
                            Toast.makeText(getApplicationContext(),
                                    e + "", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open an image", Toast.LENGTH_LONG).show();
            }

        }

    }

    public void onGroupNameClicked(View v){
        String groupNameSelected = ((TextView)v).getText().toString();
        Intent intent = new Intent(UserProfileActivity.this,
                GroupHomeActivity.class);
        intent.putExtra("groupName",groupNameSelected);
        startActivity(intent);

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
        if (id == R.id.updateProfilePicture) {
            onProfilePicClicked();
            return true;
        }
        if (id == R.id.logOut) {

            ParseUser.logOut();
            finish();
        }
//        if (id == R.id.creategroupMenuItem) {
//            Intent intent = new Intent(UserProfileActivity.this,
//                    CreateGroupActivity.class);
//            startActivity(intent);
//            return true;
//        }
        if (id == R.id.searchForGroups) {
            Intent intent = new Intent(UserProfileActivity.this,
                    SearchForGroupActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.refreshPage){
            Intent intentNewA = new Intent(UserProfileActivity.this,
                    UserProfileActivity.class);
            startActivity(intentNewA);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
