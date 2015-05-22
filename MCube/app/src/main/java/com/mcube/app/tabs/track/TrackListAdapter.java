package com.mcube.app.tabs.track;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mcube.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class TrackListAdapter extends ArrayAdapter<TrackData> {

    private Context context;
    private int layoutResource;
    ArrayList<TrackData> trackDataArrayList;
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

    public TrackListAdapter(Context context, int resource, ArrayList<TrackData> list) {
        super(context, resource, list);

        this.context = context;
        layoutResource = resource;
        trackDataArrayList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return trackDataArrayList.size();
    }

    @Override
    public TrackData getItem(int position) {
        // TODO Auto-generated method stub
        return trackDataArrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView == null && trackDataArrayList.get(position).getCallId()!=null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.track_list_item, parent, false);
            convertView.setTag(VIEW_TYPE.REAL);
        } else if(convertView == null && trackDataArrayList.get(position).getCallId()==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.loading_item, parent, false);
            convertView.setTag(VIEW_TYPE.PROG);
            return convertView;
        } else if (convertView!=null){
            if (convertView.getTag() == VIEW_TYPE.PROG && trackDataArrayList.get(position).getCallId()!=null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.track_list_item, parent, false);
                convertView.setTag(VIEW_TYPE.REAL);
            } else if (convertView.getTag() == VIEW_TYPE.REAL && trackDataArrayList.get(position).getCallId()==null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.loading_item, parent, false);
                convertView.setTag(VIEW_TYPE.PROG);
                return convertView;
            }
        }

        TrackData trackData = trackDataArrayList.get(position);

        TextView callFromTextView = (TextView) convertView.findViewById(R.id.tCallFromTextView);
        if (trackData.getCallFrom()!=null) {
            callFromTextView.setText(trackData.getCallFrom());
        }

        TextView callerNameTextView = (TextView) convertView.findViewById(R.id.tCallerNameTextView);
        if (trackData.getCallerName()!=null) {
            callerNameTextView.setText(trackData.getCallerName());
        }

        TextView groupNameTextView = (TextView) convertView.findViewById(R.id.tGroupNameTextView);
        if (trackData.getGroupName()!=null) {
            groupNameTextView.setText(trackData.getGroupName());
        }

        TextView dateTextView = (TextView) convertView.findViewById(R.id.tDateTextView);
        if (trackData.getCallTime()!=null) {
            dateTextView.setText(sdfDate.format(trackData.getCallTime()));
        }

        TextView timeTextView = (TextView) convertView.findViewById(R.id.tTimeTextView);
        if (trackData.getCallTime()!=null) {
            timeTextView.setText(sdfTime.format(trackData.getCallTime()));
        }

        TextView statusTextView = (TextView) convertView.findViewById(R.id.tStatusTextView);
        if (trackData.getStatus()!=null) {
            statusTextView.setText(trackData.getStatus());
        }

        return convertView;
    }
    private enum VIEW_TYPE {
        REAL,
        PROG
    }
}
