package com.ufl.gradeup;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GroupHomeActivity extends AppCompatActivity {
    String append;
    private Menu joinMenu;
    private String joinOrCancel = "Join";
    private String groupName;
    private ArrayList<String> groupMembersList = new ArrayList<String>();
    private ArrayList<String> groupUserNameList = new ArrayList<String>();
    private ArrayList<String> joinRequests = new ArrayList<String>();
    ArrayList<String> todayMeetingTitleList = new ArrayList<String>();
    ArrayList<String> todayMeetingTimeList = new ArrayList<String>();
    public static final int PICTURE_REQUEST = 20;
    byte[] ProfilePic;
    ParseFile pictureFile;
    ParseFile groupPicture;
    String picName = "profilePic.png";
    String name;
    byte[] profilepictureByteArray;
    private String memberName;
    private boolean isAdmin ;
    private boolean isRequestSent ;
    FloatingActionButton fab;

    ImageView groupProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_home);
        loadNavData();
        groupName = getIntent().getStringExtra("groupName");
        groupProfilePic = (ImageView) findViewById(R.id.GroupImage);
//        ParseUser parseUser = ParseUser.getCurrentUser();
//        memberName = parseUser.get("Name").toString();
        name = groupName;
        fab = (FloatingActionButton) findViewById(R.id.floatingCreateMeetingBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupHomeActivity.this,
                        CreateGroupMeetingActivity.class);
                intent.putExtra("groupName", groupName);
                intent.putExtra("memberList", groupUserNameList);
                startActivity(intent);
            }
        });
        getTodayGroupSchedule();
        getGroupMembersList();


//        try {
//            isAdmin();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
//        query.whereEqualTo("groupName", groupName);
//        query.whereEqualTo("isAdmin", 1);
//        ParseFile profilePictureFile = null; //currentUser.getParseFile("ProfilePic");
//        try {
//            profilePictureFile = query.getFirst().getParseFile("ProfilePic");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        final ProgressDialog pictureUploadProgress = new ProgressDialog(this);
//        pictureUploadProgress.setTitle("Loading Profile...");
//        pictureUploadProgress.show();
//        profilePictureFile.getDataInBackground(new GetDataCallback() {
//            @Override
//            public void done(byte[] data, ParseException e) {
//
//                if (e == null) {
//                    Bitmap profilePicBmp = BitmapFactory.decodeByteArray(data, 0, data.length);
////                    userProfilePic = (ImageView) findViewById(R.id.ProfileImage);
//                    groupProfilePic.setImageBitmap(profilePicBmp);
//                    pictureUploadProgress.dismiss();
//                } else {
//                    //userProfilePic.setImageBitmap(R.mipmap.xyz);
//                }
//            }
//        });



        Toolbar groupProfileToolbar = (Toolbar) findViewById(R.id.groupHomeToolbar);
        setSupportActionBar(groupProfileToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_groupPic);

        collapsingToolbar.setTitle(groupName);

        NavigationDrawerFragment navDrawerFrag = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drawer_fragment);
        navDrawerFrag.setUp(R.id.nav_drawer_fragment, (DrawerLayout) findViewById(R.id.group_drawer_layout), groupProfileToolbar);
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

    //TODO
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

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                query.whereEqualTo("groupName", groupName);
                query.whereEqualTo("isAdmin", 1);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        for (ParseObject object : list
                                ) {
                            if (e == null) {
                                pictureUploadProgress.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "Picture Uploaded", Toast.LENGTH_SHORT)
                                        .show();
                                object.put("ProfilePic", pictureFile);
                                groupProfilePic.setImageBitmap(PictureBitmap);
                                object.saveInBackground();
                                pictureUploadProgress.dismiss();

                            } else {
                                Toast.makeText(getApplicationContext(),
                                        e + "", Toast.LENGTH_LONG)
                                        .show();
                            }
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
                Toast.makeText(this, "Unable to open an image", Toast.LENGTH_LONG).show();
            }

        }

    }

        public void updateMenu(Menu menu){
            getMenuInflater().inflate(R.menu.menu_group_home, menu);
            MenuItem approveRequestMenuItem = menu.findItem(R.id.approveRequestListItem);
            MenuItem joinGroupListItem = menu.findItem(R.id.joinGroupListItem);
            MenuItem viewGroupSchedule = menu.findItem(R.id.viewGroupSchedule);
            MenuItem leaveGroup = menu.findItem(R.id.leaveGroup);
            MenuItem updateGroupProfilePicture = menu.findItem(R.id.updateGroupProfilePicture);

            approveRequestMenuItem.setVisible(isAdmin);

            ParseUser parseUser = ParseUser.getCurrentUser();



            //joinGroupListItem.setVisible(!groupUserNameList.contains(parseUser.getUsername()));


            if(isRequestSent){
                joinGroupListItem.setTitle("Cancel Request");
            }else{
                joinGroupListItem.setTitle("Join Group");
                joinGroupListItem.setVisible(!isAdmin && !groupUserNameList.contains(parseUser.getUsername()));
                viewGroupSchedule.setVisible(groupUserNameList.contains(parseUser.getUsername()));
                leaveGroup.setVisible(groupUserNameList.contains(parseUser.getUsername()));
                updateGroupProfilePicture.setVisible(groupUserNameList.contains(parseUser.getUsername()));
            }
        }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_group_home, menu);

        MenuItem approveRequestMenuItem = menu.findItem(R.id.approveRequestListItem);
        MenuItem joinGroupListItem = menu.findItem(R.id.joinGroupListItem);
        MenuItem viewGroupSchedule = menu.findItem(R.id.viewGroupSchedule);
        MenuItem leaveGroup = menu.findItem(R.id.leaveGroup);
        MenuItem updateGroupProfilePicture = menu.findItem(R.id.updateGroupProfilePicture);

        approveRequestMenuItem.setVisible(isAdmin);

        ParseUser parseUser = ParseUser.getCurrentUser();



        //joinGroupListItem.setVisible(!groupUserNameList.contains(parseUser.getUsername()));


        if(isRequestSent){
            joinGroupListItem.setTitle("Cancel Request");
        }else{
            joinGroupListItem.setTitle("Join Group");
            joinGroupListItem.setVisible(!isAdmin && !groupUserNameList.contains(parseUser.getUsername()));
            viewGroupSchedule.setVisible(groupUserNameList.contains(parseUser.getUsername()));
            leaveGroup.setVisible(groupUserNameList.contains(parseUser.getUsername()));
            updateGroupProfilePicture.setVisible(groupUserNameList.contains(parseUser.getUsername()));
        }
        int a = 1;

        joinMenu = menu;
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_home, menu);
//
//        MenuItem approveRequestMenuItem = menu.findItem(R.id.approveRequestListItem);
//        MenuItem joinGroupListItem = menu.findItem(R.id.joinGroupListItem);
//        MenuItem viewGroupSchedule = menu.findItem(R.id.viewGroupSchedule);
//        MenuItem leaveGroup = menu.findItem(R.id.leaveGroup);
//        MenuItem updateGroupProfilePicture = menu.findItem(R.id.updateGroupProfilePicture);
//
//        approveRequestMenuItem.setVisible(isAdmin);
//
//        ParseUser parseUser = ParseUser.getCurrentUser();
//
//
//
//        //joinGroupListItem.setVisible(!groupUserNameList.contains(parseUser.getUsername()));
//
//
//        if(isRequestSent){
//            joinGroupListItem.setTitle("Cancel Request");
//        }else{
//            joinGroupListItem.setTitle("Join Group");
//            joinGroupListItem.setVisible(!isAdmin && !groupUserNameList.contains(parseUser.getUsername()));
//            viewGroupSchedule.setVisible(groupUserNameList.contains(parseUser.getUsername()));
//            leaveGroup.setVisible(groupUserNameList.contains(parseUser.getUsername()));
//            updateGroupProfilePicture.setVisible(groupUserNameList.contains(parseUser.getUsername()));
//        }
//        int a = 1;
//
//        joinMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.new_meeting) {
//            Intent intent = new Intent(
//                    GroupHomeActivity.this,
//                    CreateGroupMeetingActivity.class);
//            intent.putExtra("groupName", groupName);
//            intent.putExtra("memberList", groupUserNameList);
//            startActivity(intent);
//            return true;
//        }

        if (id == R.id.joinGroupListItem) {
            ParseUser parseUser = ParseUser.getCurrentUser();
            memberName = parseUser.get("Name").toString();
            if (joinOrCancel.equalsIgnoreCase("Join")) {
                item.setTitle("Cancel Request");
                String test = getIntent().getStringExtra("groupName");
                final ParseUser requestingUser = ParseUser.getCurrentUser();
                final String joinRequest = requestingUser.getUsername() + "," + groupName + "," + requestingUser.get("Name");
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                query.whereEqualTo("groupName", getIntent().getStringExtra("groupName"));
                query.whereEqualTo("isAdmin", 1);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        for (ParseObject object : list
                                ) {

                            object.addAllUnique("joinRequests", Arrays.asList(joinRequest));

                            object.saveInBackground();
                            joinRequests = (ArrayList)object.get("joinRequests");
//                           if(object.getString("joinRequests") == null){
//                            object.put("joinRequests",requestingUser.getUsername());
//                            object.saveInBackground();
//                        }else{
//                            append = object.getString("joinRequests")+","+requestingUser.getUsername();
//                            object.put("joinRequests",append);
//                            object.saveInBackground();
//                        }
                        }

                    }
                });

                joinOrCancel = "Cancel";
            } else if (joinOrCancel.equalsIgnoreCase("Cancel")) {
                item.setTitle("Join Group");
                joinOrCancel = "Join";

                final ParseUser requestingUser = ParseUser.getCurrentUser();

                final String joinRequest = requestingUser.getUsername() + "," + groupName + "," + requestingUser.get("Name");
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                query.whereEqualTo("groupName", getIntent().getStringExtra("groupName"));
                query.whereEqualTo("isAdmin", 1);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        for (ParseObject object : list
                                ) {
                            object.removeAll("joinRequests", Arrays.asList(joinRequest));
                            object.saveInBackground();
                        }
                    }
                });
            }

            return true;
        }

        if (id == R.id.approveRequestListItem) {


            Intent intent = new Intent(
                    GroupHomeActivity.this,
                    ApproveRequestActivity.class);
            intent.putExtra("groupName", groupName);
            intent.putExtra("memberName", memberName);
            startActivity(intent);


        }

        if (id == R.id.updateGroupProfilePicture) {
            onProfilePicClicked();
        }

        if(id == R.id.leaveGroup){
            leaveGroup();
        }

        if (id == R.id.viewGroupSchedule) {
            Intent intentNewA = new Intent(GroupHomeActivity.this,
                    ActivityViewGroupSchedule.class);
            intentNewA.putExtra("groupName", groupName);
            startActivity(intentNewA);
        }

        if(id == R.id.refreshPage){
            Intent intentNewA = new Intent(GroupHomeActivity.this,
                    GroupHomeActivity.class);
            intentNewA.putExtra("groupName", groupName);
            startActivity(intentNewA);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //Need to Rename function
    private void isAdmin() throws ParseException {

        final ParseUser requestingUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("groupName", getIntent().getStringExtra("groupName"));
        query.whereEqualTo("isAdmin", 1);
        ArrayList<String> requestArray = null;
        try {
            query.fromLocalDatastore();
            requestArray = (ArrayList<String>)query.getFirst().get("joinRequests");
            if(requestArray.contains(requestingUser.getUsername()+","+groupName+","+requestingUser.get("Name").toString())){
                isRequestSent = true;
            }else{
                isRequestSent = false;
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        query.whereEqualTo("userName", requestingUser.getUsername());

        if (query.count() == 1) {
//            return true;
            isAdmin = true;
        }else {
//        return false;
            isAdmin = false;
        }
    }

    public void getGroupMembersList() {
         final ParseUser requestingUser = ParseUser.getCurrentUser();
         ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("groupName", groupName);
//        query.whereEqualTo("isAdmin", 1);


//        query.whereEqualTo("userName",requestingUser.getUsername() );
//        try {
//            if(query.count() == 1){
//                isAdmin = true;
//            } else{
//                isAdmin = false;
//            }
//        } catch (ParseException e1) {
//            e1.printStackTrace();
//        }
        final ProgressDialog pictureLoadProgress = new ProgressDialog(this);
        pictureLoadProgress.setTitle("Loading Group Profile...");
        pictureLoadProgress.show();
//
//        try {
//            for(ParseObject object :query.find()){
//                if (!groupMembersList.contains(object.getString("memberName"))) {
//                    groupMembersList.add(object.getString("memberName"));
//                    groupUserNameList.add(object.getString("userName"));
//
//
//                    if (object.getNumber("isAdmin") == 1) {
//
//
//                        ParseFile profilePictureFile = object.getParseFile("ProfilePic");
//                        try {
//                            profilepictureByteArray = profilePictureFile.getData();
//                        } catch (ParseException e1) {
//                            e1.printStackTrace();
//                        }
//                        Bitmap profilePicBmp = BitmapFactory.decodeByteArray(profilepictureByteArray, 0, profilepictureByteArray.length);
//                        groupProfilePic.setImageBitmap(profilePicBmp);
//
//                        ArrayList<String> requestArray = null;
//                        requestArray = (ArrayList<String>) object.get("joinRequests");
//                        if (requestArray.contains(requestingUser.getUsername() + "," + groupName + "," + requestingUser.get("Name").toString())) {
//                            isRequestSent = true;
//                        } else {
//                            isRequestSent = false;
//                        }
//
//
////                        if (object.getString("userName") == requestingUser.getUsername()) {
//////            return true;
////                            isAdmin = true;
////                        } else {
//////        return false;
////                            isAdmin = false;
////                        }
//                    }
//                }
//            }
//            query.whereEqualTo("isAdmin",1);
//            query.whereEqualTo("userName", requestingUser.getUsername());
//            if (query.count() == 1) {
////            return true;
//                isAdmin = true;
//            }else {
////        return false;
//                isAdmin = false;
//            }
//
//            pictureLoadProgress.dismiss();
//            bindGroupMembers();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject object : list
                            ) {
                        if (!groupMembersList.contains(object.getString("memberName"))) {
                            groupMembersList.add(object.getString("memberName"));
                            groupUserNameList.add(object.getString("userName"));


                            if (object.getNumber("isAdmin") == 1) {


                                ParseFile profilePictureFile = object.getParseFile("ProfilePic");
                                try {
                                    profilepictureByteArray = profilePictureFile.getData();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                Bitmap profilePicBmp = BitmapFactory.decodeByteArray(profilepictureByteArray, 0, profilepictureByteArray.length);
                                groupProfilePic.setImageBitmap(profilePicBmp);

                                ArrayList<String> requestArray = null;
                                requestArray = (ArrayList<String>) object.get("joinRequests");
                                if (requestArray.contains(requestingUser.getUsername() + "," + groupName + "," + requestingUser.get("Name").toString())) {
                                    isRequestSent = true;
                                } else {
                                    isRequestSent = false;
                                }


                                if (object.getString("userName").equals(requestingUser.getUsername())) {
//            return true;
                                    isAdmin = true;
                                } else {
//        return false;
                                    isAdmin = false;
                                }
                            }
                        }
                    }
                    ParseUser parseUser = ParseUser.getCurrentUser();
                    if(!groupUserNameList.contains(parseUser.getUsername())) {
                        fab.setVisibility(View.GONE);
                    }
                    bindGroupMembers();
                    pictureLoadProgress.dismiss();

                }
            }
        });




    }

    public void bindGroupMembers() {

        LinearLayout groupLayout = (LinearLayout) findViewById(R.id.membersCard);
        int[] icons = {R.mipmap.male_icon,R.mipmap.male1_icon,R.mipmap.female_icon,R.mipmap.boy_icon,R.mipmap.girl_icon};
        if (groupMembersList.size() == 0) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setTypeface(textView.getTypeface(), Typeface.ITALIC);
            textView.setText("Nothing to Show...");
            if (Build.VERSION.SDK_INT < 23) {
                textView.setTextAppearance(this, android.R.style.TextAppearance_Material_Small);
            } else {
                textView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
            }
            groupLayout.addView(textView);
        } else {
            for (int i = 0; i < groupMembersList.size(); i++) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                textView.setText("  " + groupMembersList.get(i));
                textView.setFocusable(true);
                textView.setClickable(true);
                textView.setCompoundDrawablesWithIntrinsicBounds(icons[new Random().nextInt(icons.length)], 0, 0, 0);
                textView.setPadding(0,20,0,20);
                if (Build.VERSION.SDK_INT < 23) {
                    textView.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
                } else {
                    textView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Medium);
                }
                groupLayout.addView(textView);
                if (i < groupMembersList.size() - 1) {
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

    public void loadNavData() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        final String currentName = currentUser.getString("Name");

        TextView navTitle = (TextView) findViewById(R.id.navTitleText);
        navTitle.setText(currentName);

        ParseFile profilePictureFile = currentUser.getParseFile("ProfilePic");

        profilePictureFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {

                if (e == null) {
                    Bitmap profilePicBmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//                    userProfilePic = (ImageView) findViewById(R.id.ProfileImage);
                    ImageView navProfilePic = (ImageView) findViewById(R.id.navImage);
                    navProfilePic.setImageBitmap(profilePicBmp);

                } else {

                }
            }
        });

    }

    //Can remove if Join Request and Appprove REquest works Fine
    private boolean isRequestSent() throws ParseException {
        final int flag;
        final ParseUser requestingUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("groupName", getIntent().getStringExtra("groupName"));
        query.whereEqualTo("isAdmin", 1);
        ArrayList<String> requestArray = (ArrayList<String>)query.getFirst().get("joinRequests");
        if(requestArray.contains(requestingUser.getUsername()+","+groupName+","+requestingUser.get("Name").toString())){
            return true;
        }else{
            return false;
        }


    }

    private void leaveGroup(){

        Toast.makeText(getApplicationContext(),
                "Left Group", Toast.LENGTH_SHORT)
                .show();
        if(isAdmin){
            isAdmin = false;
            setNewAdmin();
        }
        final ParseUser requestingUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("groupName",groupName);
        query.whereEqualTo("userName", requestingUser.getUsername());
        try {
            query.getFirst().deleteInBackground();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        Intent intent = new Intent(
                GroupHomeActivity.this,
                UserProfileActivity.class);
        startActivity(intent);
        finish();
    }

    //Create dynamic UI from today's Group schedule
    public void bindTodaySchedule() {
        LinearLayout scheduleLayout = (LinearLayout) findViewById(R.id.todayMeetingCard);
        if (todayMeetingTitleList.size() == 0) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setTypeface(textView.getTypeface(), Typeface.ITALIC);
            textView.setText("Nothing to Show...");
            if (Build.VERSION.SDK_INT < 23) {
                textView.setTextAppearance(this, android.R.style.TextAppearance_Material_Small);
            } else {
                textView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
            }
            scheduleLayout.addView(textView);
        } else {
            for (int i = 0; i < todayMeetingTitleList.size(); i++) {
                TextView titletextView = new TextView(this);
                TextView timeTextView = new TextView(this);

                titletextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                titletextView.setText(todayMeetingTitleList.get(i));
                titletextView.setFocusable(true);
                titletextView.setClickable(true);

                timeTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                timeTextView.setText(todayMeetingTimeList.get(i));
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

                if (i < todayMeetingTitleList.size() - 1) {
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

    //Retrieve today's group Schedule from Parse
    public void getTodayGroupSchedule() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MemberSchedule");
        query.whereEqualTo("groupName", groupName);
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
                            String schName = "" + object.getString("meetingTitle");
                            String schTime = object.getString("meetingStartTime") +
                                    " to " + object.getString("meetingEndTime");
                            todayMeetingTitleList.add(schName);
                            todayMeetingTimeList.add(schTime);
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
    private void setNewAdmin(){
        final ParseUser requestingUser = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> queryAdmin = ParseQuery.getQuery("Groups");
        queryAdmin.whereEqualTo("groupName", groupName);
        queryAdmin.whereEqualTo("isAdmin", 1);



        queryAdmin.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                groupPicture = parseObject.getParseFile("ProfilePic");
                joinRequests = (ArrayList<String>) parseObject.get("joinRequests");
            }
        });
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("groupName", groupName);
        query.whereEqualTo("isAdmin", 0);
        try {
            if (!(query.count() == 0)) {
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        parseObject.put("isAdmin",1);
                        parseObject.put("ProfilePic",groupPicture);
                        parseObject.put("joinRequests",joinRequests);
                        parseObject.saveInBackground();
                    }
                });

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }



}
