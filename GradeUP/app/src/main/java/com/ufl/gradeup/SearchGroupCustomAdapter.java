package com.ufl.gradeup;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parikshitt23 on 10-11-2015.
 */
public class SearchGroupCustomAdapter extends BaseAdapter implements ListAdapter {
    private List<String> list = new ArrayList<String>();
    private Context context;
    private boolean flag = true;
    public SearchGroupCustomAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
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
            view = inflater.inflate(R.layout.search_group_listview, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.listItemString);
        listItemText.setText(list.get(position));

        final Button joinBtn = (Button)view.findViewById(R.id.joinGroupListItem);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(flag){

                joinBtn.setText("Cancel Request");
                flag = false;
                notifyDataSetChanged();
            }else {
                joinBtn.setText("Join");
                flag = true;
             }
            }
        });
        return view;
    }
}
