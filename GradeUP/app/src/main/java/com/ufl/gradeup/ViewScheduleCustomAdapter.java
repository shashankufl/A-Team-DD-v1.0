package com.ufl.gradeup;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shashank on 11/11/2015.
 */
public class ViewScheduleCustomAdapter<S> extends BaseAdapter implements ListAdapter, AppCompatCallback {
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
        final TextView listItemText = (TextView)view.findViewById(R.id.listItemString);
        listItemText.setText(list.get(position));


        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.deleteListItem);
        //ImageButton editBtn = (ImageButton)view.findViewById(R.id.scheduleListView);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Object obj = listItemText.getText();
//                String sample = obj.toString();
                list.remove(position);
                notifyDataSetChanged();

            }
        });
        return view;
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
}
