package com.example.shashank.logintest;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        // Add your initialization code here
        Parse.initialize(this, "qjw4bsaKO81FiyYE97V2QepNZ0f6WZVnq0luselk", "KTwVE5u3YDlOrwePbfMDAKzNf4mSCSrWkqfXh6kk");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }

}