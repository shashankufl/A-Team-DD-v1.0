package com.ufl.gradeup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupHomeActivity extends AppCompatActivity {
    String append;
    private Menu joinMenu;
    private String joinOrCancel = "Join";
    private String groupName;
    private ArrayList<String> groupMembersList = new ArrayList<String>();
    private ArrayList<String> groupUserNameList = new ArrayList<String>();
    public static final int PICTURE_REQUEST = 20;
    byte[] ProfilePic;
    ParseFile pictureFile;
    String picName = "profilePic.png";
    String name;
    byte[] profilepictureByteArray;
    private String memberName;

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
        getGroupMembersList();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_home, menu);

        MenuItem approveRequestMenuItem = menu.findItem(R.id.approveRequestListItem);
        MenuItem joinGroupListItem = menu.findItem(R.id.joinGroupListItem);

        try {
            approveRequestMenuItem.setVisible(isAdmin());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseUser parseUser = ParseUser.getCurrentUser();

        try {
            if(isRequestSent()){
                joinGroupListItem.setTitle("Cancel Request");
            }else{
                joinGroupListItem.setTitle("Join Group");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //joinGroupListItem.setVisible(!groupUserNameList.contains(parseUser.getUsername()));
        try {
            joinGroupListItem.setVisible(!isAdmin() && !groupUserNameList.contains(parseUser.getUsername()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int a = 1;

        joinMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_meeting) {
            Intent intent = new Intent(
                    GroupHomeActivity.this,
                    CreateGroupMeetingActivity.class);
            intent.putExtra("groupName", groupName);
            intent.putExtra("memberList", groupUserNameList);
            startActivity(intent);
            return true;
        }

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

        return super.onOptionsItemSelected(item);
    }

    private boolean isAdmin() throws ParseException {

        final ParseUser requestingUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("groupName", getIntent().getStringExtra("groupName"));
        query.whereEqualTo("isAdmin", 1);
        query.whereEqualTo("userName", requestingUser.getUsername());
        if (query.count() == 1) {
            return true;
        }
        return false;
    }

    public void getGroupMembersList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("groupName", groupName);
        final ProgressDialog pictureLoadProgress = new ProgressDialog(this);
        pictureLoadProgress.setTitle("Loading Group Profile...");
        pictureLoadProgress.show();
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
                            }
                        }
                    }
                    pictureLoadProgress.dismiss();
                    bindGroupMembers();
                }
            }
        });

    }

    public void bindGroupMembers() {

        LinearLayout groupLayout = (LinearLayout) findViewById(R.id.membersCard);
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
                textView.setText("* " + groupMembersList.get(i));
                textView.setFocusable(true);
                textView.setClickable(true);
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
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> list, ParseException e) {
//                for (ParseObject object : list
//                        ) {
//                    ArrayList<String> requestArray = (ArrayList<String>) object.get("joinRequests");
//                    if (requestArray.contains(requestingUser.getUsername())) {
//                       flag = 1;
//
//                    }
//                }
//            }
//        });

    }

}
