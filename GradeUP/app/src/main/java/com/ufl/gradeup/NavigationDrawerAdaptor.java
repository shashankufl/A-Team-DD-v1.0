package com.ufl.gradeup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Nikhil on 04/11/2015.
 */
public class NavigationDrawerAdaptor extends RecyclerView.Adapter<NavigationDrawerAdaptor.NavDrawerViewHolder> {

    private LayoutInflater inflater;
    List<NavigationDrawerInformation> navData = Collections.emptyList();

    public NavigationDrawerAdaptor(Context context, List<NavigationDrawerInformation> data) {
        inflater = LayoutInflater.from(context);
        this.navData=data;

    }

    @Override
    public NavDrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.nav_customview, parent, false);
        NavDrawerViewHolder holder = new NavDrawerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NavDrawerViewHolder holder, int position) {
        NavigationDrawerInformation currentData = navData.get(position);
        holder.navTitle.setText(currentData.title);
        holder.navIcon.setImageResource(currentData.iconId);
    }

    @Override
    public int getItemCount() {
        return navData.size();
    }

    class NavDrawerViewHolder extends RecyclerView.ViewHolder {
        TextView navTitle;
        ImageView navIcon;

        public NavDrawerViewHolder(View itemView) {
            super(itemView);
            navTitle= (TextView)itemView.findViewById(R.id.navItemText);
            navIcon= (ImageView)itemView.findViewById(R.id.navItemIcon);

        }
    }
}
