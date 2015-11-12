package com.ufl.gradeup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shashank on 11/11/2015.
 */
public class ViewScheduleCustomAdapter<S> extends BaseAdapter implements ListAdapter {
    private List<String> list = new ArrayList<String>();
    private Context context;

    public ViewScheduleCustomAdapter(List<String> schedule, ViewScheduleActivity viewScheduleActivity) {
        this.list = schedule;
        this.context = viewScheduleActivity;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            view = inflater.inflate(R.layout.create_group_listview, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.listItemString);
        listItemText.setText(list.get(position));

        return view;
    }

}
