package com.ufl.gradeup;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

//import android.support.v4.app.Fragment;




/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends android.support.v4.app.Fragment implements NavigationDrawerAdaptor.ClickListener{

    private ActionBarDrawerToggle navDrawToggle;
    private DrawerLayout navDrawerLayout;
    public static final String PREF_FILE_NAME="navPref";
    public static final String KEY_NAV_USER_LEARNED="nav_user_learned";
    private boolean navUserLearned;
    private boolean navFromSavedInstanceState;
    private View navContainer;
    private NavigationDrawerAdaptor adaptor;
    private RecyclerView navRecyclerView;

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
        View navLayout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        navRecyclerView = (RecyclerView)navLayout.findViewById(R.id.nav_drawerList);
        adaptor = new NavigationDrawerAdaptor(getActivity(),getNavData());
        adaptor.setClickListener(this);
        navRecyclerView.setAdapter(adaptor);
        navRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return navLayout;
    }

    public static List<NavigationDrawerInformation> getNavData(){
        List<NavigationDrawerInformation> navData = new ArrayList<>();
        int[] navIcons ={R.mipmap.navmyprofile,R.mipmap.navupdateprofile,R.mipmap.navaddschedule,R.mipmap.navcreategroup,R.mipmap.navjoingroup,R.mipmap.navhelp,R.mipmap.navaboutus,R.mipmap.navlogout};

        String[] navText = {"My Profile", "Update Profile","View Schedule","Add Schedule", "Create Study Group", "Search Study Group","Help","About Us", "Log Out"};

        for(int i= 0;i<navIcons.length&&i<navText.length;i++){
            NavigationDrawerInformation current = new NavigationDrawerInformation();
            current.iconId= navIcons[i];
            current.title=navText[i];
            navData.add(current);
        }
        return navData;

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

    @Override
    public void itemClicked(View view, int position) {

        //Write code for navigation using position
        if(position==0){
            startActivity(new Intent(getActivity(),UserProfileActivity.class));
        }else if(position==1){
            startActivity(new Intent(getActivity(),UpdateProfileActivity.class));
        } else if(position==3){
            startActivity(new Intent(getActivity(),AddScheduleActivity.class));
        } else if(position==4){
            startActivity(new Intent(getActivity(),CreateGroupActivity.class));
        } else if(position==5){
            startActivity(new Intent(getActivity(),SearchForGroupActivity.class));
        } else if(position==2){
            startActivity(new Intent(getActivity(),ViewScheduleActivity.class));
        }



        //startActivity(new Intent(getActivity(),RegisterActivity.class));
    }
}
