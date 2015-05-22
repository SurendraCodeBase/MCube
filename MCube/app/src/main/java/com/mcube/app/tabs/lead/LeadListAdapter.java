package com.mcube.app.tabs.lead;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mcube.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class LeadListAdapter extends ArrayAdapter<LeadData> {

    private Context context;
    private int layoutResource;
    ArrayList<LeadData> leadDataArrayList;
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

    public LeadListAdapter(Context context, int resource, ArrayList<LeadData> list) {
        super(context, resource, list);

        this.context = context;
        layoutResource = resource;
        leadDataArrayList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return leadDataArrayList.size();
    }

    @Override
    public LeadData getItem(int position) {
        // TODO Auto-generated method stub
        return leadDataArrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView == null && leadDataArrayList.get(position).getCallId()!=null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lead_list_item, parent, false);
            convertView.setTag(VIEW_TYPE.REAL);
        } else if(convertView == null && leadDataArrayList.get(position).getCallId()==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.loading_item, parent, false);
            convertView.setTag(VIEW_TYPE.PROG);
            return convertView;
        } else if (convertView!=null){
            if (convertView.getTag() == VIEW_TYPE.PROG && leadDataArrayList.get(position).getCallId()!=null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.lead_list_item, parent, false);
                convertView.setTag(VIEW_TYPE.REAL);
            } else if (convertView.getTag() == VIEW_TYPE.REAL && leadDataArrayList.get(position).getCallId()==null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.loading_item, parent, false);
                convertView.setTag(VIEW_TYPE.PROG);
                return convertView;
            }
        }

        LeadData leadData = leadDataArrayList.get(position);


        TextView callFromTextView = (TextView) convertView.findViewById(R.id.leadCallFromTextView);
        if (leadData.getCallFrom()!=null) {
            callFromTextView.setText(leadData.getCallFrom());
        }

        TextView callerNameTextView = (TextView) convertView.findViewById(R.id.leadCallerNameTextView);
        if (leadData.getCallerName()!=null) {
            callerNameTextView.setText(leadData.getCallerName());
        }

        TextView groupNameTextView = (TextView) convertView.findViewById(R.id.leadGroupNameTextView);
        if (leadData.getGroupName()!=null) {
            groupNameTextView.setText(leadData.getGroupName());
        }

        TextView dateTextView = (TextView) convertView.findViewById(R.id.leadDateTextView);
        if (leadData.getCallTime()!=null) {
            dateTextView.setText(sdfDate.format(leadData.getCallTime()));
        }

        TextView timeTextView = (TextView) convertView.findViewById(R.id.leadTimeTextView);
        if (leadData.getCallTime()!=null) {
            timeTextView.setText(sdfTime.format(leadData.getCallTime()));
        }

        TextView statusTextView = (TextView) convertView.findViewById(R.id.leadStatusTextView);
        if (leadData.getStatus()!=null) {
            statusTextView.setText(leadData.getStatus());
        }

        return convertView;
    }

    private enum VIEW_TYPE {
        REAL,
        PROG
    }
}
