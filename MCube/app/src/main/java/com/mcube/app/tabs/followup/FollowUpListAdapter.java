package com.mcube.app.tabs.followup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mcube.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class FollowUpListAdapter extends ArrayAdapter<FollowUpData> {

    private Context context;
    private int layoutResource;
    ArrayList<FollowUpData> followUpDataArrayList;
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

    public FollowUpListAdapter(Context context, int resource, ArrayList<FollowUpData> list) {
        super(context, resource, list);

        this.context = context;
        layoutResource = resource;
        followUpDataArrayList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return followUpDataArrayList.size();
    }

    @Override
    public FollowUpData getItem(int position) {
        // TODO Auto-generated method stub
        return followUpDataArrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView == null && followUpDataArrayList.get(position).getCallId()!=null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.follow_up_list_item, parent, false);
            convertView.setTag(VIEW_TYPE.REAL);
        } else if(convertView == null && followUpDataArrayList.get(position).getCallId()==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.loading_item, parent, false);
            convertView.setTag(VIEW_TYPE.PROG);
            return convertView;
        } else if (convertView!=null){
            if (convertView.getTag() == VIEW_TYPE.PROG && followUpDataArrayList.get(position).getCallId()!=null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.follow_up_list_item, parent, false);
                convertView.setTag(VIEW_TYPE.REAL);
            } else if (convertView.getTag() == VIEW_TYPE.REAL && followUpDataArrayList.get(position).getCallId()==null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.loading_item, parent, false);
                convertView.setTag(VIEW_TYPE.PROG);
                return convertView;
            }
        }

        FollowUpData followUpData = followUpDataArrayList.get(position);


        TextView callFromTextView = (TextView) convertView.findViewById(R.id.fCallFromTextView);
        if (followUpData.getCallFrom()!=null) {
            callFromTextView.setText(followUpData.getCallFrom());
        }

        TextView callerNameTextView = (TextView) convertView.findViewById(R.id.fCallerNameTextView);
        if (followUpData.getCallerName()!=null) {
            callerNameTextView.setText(followUpData.getCallerName());
        }

        TextView groupNameTextView = (TextView) convertView.findViewById(R.id.fGroupNameTextView);
        if (followUpData.getGroupName()!=null) {
            groupNameTextView.setText(followUpData.getGroupName());
        }

        TextView dateTextView = (TextView) convertView.findViewById(R.id.fDateTextView);
        if (followUpData.getCallTime()!=null) {
            dateTextView.setText(sdfDate.format(followUpData.getCallTime()));
        }

        TextView timeTextView = (TextView) convertView.findViewById(R.id.fTimeTextView);
        if (followUpData.getCallTime()!=null) {
            timeTextView.setText(sdfTime.format(followUpData.getCallTime()));
        }

        TextView statusTextView = (TextView) convertView.findViewById(R.id.fStatusTextView);
        if (followUpData.getStatus()!=null) {
            statusTextView.setText(followUpData.getStatus());
        }

        return convertView;
    }

    private enum VIEW_TYPE {
        REAL,
        PROG
    }
}
