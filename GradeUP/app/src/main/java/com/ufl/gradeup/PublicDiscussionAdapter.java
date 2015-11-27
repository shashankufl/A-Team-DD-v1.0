package com.ufl.gradeup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ravi Akash on 11/21/2015.
 */
public class PublicDiscussionAdapter extends BaseExpandableListAdapter {
    private List<String> headers= new ArrayList<>();
    private HashMap<String,List<String>> comments= new HashMap<String,List<String>>();
    private Context context;
    String c="xyz";
    public PublicDiscussionAdapter(Context context,List<String> headers,HashMap<String,List<String>> comments){
        this.context = context;
        this.headers = headers;
        this.comments = comments;
    }
    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return comments.get(headers.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return comments.get(headers.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title= (String) this.getGroup(groupPosition);
        if(convertView == null){            //check if the view already exists
            LayoutInflater layoutInflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= layoutInflater.inflate(R.layout.public_discussion_headers,null);
        }
        TextView headingView= (TextView) convertView.findViewById(R.id.public_discussion_heading);
        headingView.setTypeface(null, Typeface.BOLD); //to make headings bold
        headingView.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String title= (String) this.getChild(groupPosition, childPosition);
        if(convertView == null){            //check if the view already exists
            LayoutInflater layoutInflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= layoutInflater.inflate(R.layout.public_discussion_comments,null);
        }
        final TextView commentView= (TextView) convertView.findViewById(R.id.public_discussion_comment);
        commentView.setText(title);
        Button button = (Button)convertView.findViewById(R.id.postCommentButton);
        final EditText newComment=(EditText)convertView.findViewById(R.id.newComment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ((PublicDiscussionActivity)context).appendTheComment(groupPosition, newComment.getText().toString());
//               Toast.makeText(context,newComment.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
