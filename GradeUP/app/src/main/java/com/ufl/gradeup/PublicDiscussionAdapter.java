package com.ufl.gradeup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private List<String> postedBy= new ArrayList<>();
    public PublicDiscussionAdapter(Context context,List<String> headers,HashMap<String,List<String>> comments,List<String> postedBy){
        this.context = context;
        this.headers = headers;
        this.comments = comments;
        this.postedBy = postedBy;
    }
    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return comments.get(headers.get(groupPosition)).size();
    }

    public Object getPostedBy(int groupPosition) {
        return postedBy.get(groupPosition);
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
        TextView postedByView= (TextView) convertView.findViewById(R.id.public_discussion_postedBy);
        String thisCommentPostedBy= (String) this.getPostedBy(groupPosition);
        postedByView.setText(thisCommentPostedBy);
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
        final EditText addComment= (EditText) convertView.findViewById(R.id.newComment);
        addComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (addComment.getRight() - addComment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        ((PublicDiscussionActivity)context).appendTheComment(groupPosition, addComment.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
