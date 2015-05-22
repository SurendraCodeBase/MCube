package com.mcube.app.tabs.ivrs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mcube.app.R;
import com.mcube.app.tabs.ivrs.IVRSData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class IVRSListAdapter extends ArrayAdapter<IVRSData> {

    private Context context;
    private int layoutResource;
    ArrayList<IVRSData> ivrsDataArrayList;
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

    public IVRSListAdapter(Context context, int resource, ArrayList<IVRSData> list) {
        super(context, resource, list);

        this.context = context;
        layoutResource = resource;
        ivrsDataArrayList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ivrsDataArrayList.size();
    }

    @Override
    public IVRSData getItem(int position) {
        // TODO Auto-generated method stub
        return ivrsDataArrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView == null && ivrsDataArrayList.get(position).getCallId()!=null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ivrs_list_item, parent, false);
            convertView.setTag(VIEW_TYPE.REAL);
        } else if(convertView == null && ivrsDataArrayList.get(position).getCallId()==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.loading_item, parent, false);
            convertView.setTag(VIEW_TYPE.PROG);
            return convertView;
        } else if (convertView!=null){
            if (convertView.getTag() == VIEW_TYPE.PROG && ivrsDataArrayList.get(position).getCallId()!=null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.ivrs_list_item, parent, false);
                convertView.setTag(VIEW_TYPE.REAL);
            } else if (convertView.getTag() == VIEW_TYPE.REAL && ivrsDataArrayList.get(position).getCallId()==null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.loading_item, parent, false);
                convertView.setTag(VIEW_TYPE.PROG);
                return convertView;
            }
        }

        IVRSData ivrsSData = ivrsDataArrayList.get(position);


        TextView callFromTextView = (TextView) convertView.findViewById(R.id.ivrsCallFromTextView);
        if (ivrsSData.getCallFrom()!=null) {
            callFromTextView.setText(ivrsSData.getCallFrom());
        }

        TextView callerNameTextView = (TextView) convertView.findViewById(R.id.ivrsCallerNameTextView);
        if (ivrsSData.getCallerName()!=null) {
            callerNameTextView.setText(ivrsSData.getCallerName());
        }

        TextView groupNameTextView = (TextView) convertView.findViewById(R.id.ivrsGroupNameTextView);
        if (ivrsSData.getGroupName()!=null) {
            groupNameTextView.setText(ivrsSData.getGroupName());
        }

        TextView dateTextView = (TextView) convertView.findViewById(R.id.ivrsDateTextView);
        if (ivrsSData.getCallTime()!=null) {
            dateTextView.setText(sdfDate.format(ivrsSData.getCallTime()));
        }

        TextView timeTextView = (TextView) convertView.findViewById(R.id.ivrsTimeTextView);
        if (ivrsSData.getCallTime()!=null) {
            timeTextView.setText(sdfTime.format(ivrsSData.getCallTime()));
        }

        TextView statusTextView = (TextView) convertView.findViewById(R.id.ivrsStatusTextView);
        if (ivrsSData.getStatus()!=null) {
            statusTextView.setText(ivrsSData.getStatus());
        }

        return convertView;
    }

    private enum VIEW_TYPE {
        REAL,
        PROG
    }
}
