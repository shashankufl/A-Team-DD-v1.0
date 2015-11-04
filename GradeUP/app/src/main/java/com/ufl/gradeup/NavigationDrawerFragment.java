package com.ufl.gradeup;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends android.support.v4.app.Fragment {

    private ActionBarDrawerToggle navDrawToggle;
    private DrawerLayout navDrawerLayout;
    public static final String PREF_FILE_NAME="navPref";
    public static final String KEY_NAV_USER_LEARNED="nav_user_learned";
    private boolean navUserLearned;
    private boolean navFromSavedInstanceState;
    private View navContainer;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navUserLearned =Boolean.valueOf(readFromPreference(getActivity(),KEY_NAV_USER_LEARNED, "false"));
        if(savedInstanceState!=null){
            navFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }


    public void setUp(int fragID,DrawerLayout drawerLayout, android.support.v7.widget.Toolbar toolbar) {
        navDrawerLayout =drawerLayout;
        navContainer =getActivity().findViewById(fragID);
        navDrawToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!navUserLearned){
                    navUserLearned = true;
                    saveTopreference(getActivity(),KEY_NAV_USER_LEARNED,navUserLearned+"");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }


        };
//        if(!navUserLearned && !navFromSavedInstanceState){
//            navDrawerLayout.openDrawer(navContainer);
//        }
        navDrawerLayout.setDrawerListener(navDrawToggle);
        navDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                navDrawToggle.syncState();
            }
        });
    }

    public static void saveTopreference(Context context, String prefName, String prefValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor navEditor = sharedPreferences.edit();
        navEditor.putString(prefName,prefValue);
        navEditor.apply();
    }

    public static String readFromPreference(Context context, String prefName, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(prefName,defaultValue);
    }

}
