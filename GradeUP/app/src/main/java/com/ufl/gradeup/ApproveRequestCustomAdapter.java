package com.ufl.gradeup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by parikshitt23 on 12-11-2015.
 */
public class ApproveRequestCustomAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private String groupName;

    public ApproveRequestCustomAdapter(ArrayList<String> list, Context context, String groupName) {
        this.list = list;
        this.context = context;
        this.groupName = groupName;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.approve_request_listview, null);
        }

        //Handle TextView and display string from your list


        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners

        Button deleteBtn = (Button)view.findViewById(R.id.declineRequestBtn);
        Button addBtn = (Button)view.findViewById(R.id.approveRequestBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

                final String joinRequest = list.get(position);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                query.whereEqualTo("groupName", groupName);
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
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String userName = list.get(position).split(",")[0];
                final String joinRequest = list.get(position);
                ParseObject group = new ParseObject("Groups");
                group.put("groupName", groupName);
                group.put("userName", userName);
                group.put("isAdmin",0);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                query.whereEqualTo("groupName", groupName);
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
                group.saveInBackground();
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
