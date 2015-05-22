package com.mcube.app.tabs.x;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mcube.app.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class XListAdapter extends ArrayAdapter<XData> {

    private Context context;
    private int layoutResource;
    ArrayList<XData> xDataArrayList;
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

    public XListAdapter(Context context, int resource, ArrayList<XData> list) {
        super(context, resource, list);

        this.context = context;
        layoutResource = resource;
        xDataArrayList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return xDataArrayList.size();
    }

    @Override
    public XData getItem(int position) {
        // TODO Auto-generated method stub
        return xDataArrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView == null && xDataArrayList.get(position).getCallId()!=null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.x_list_item, parent, false);
            convertView.setTag(VIEW_TYPE.REAL);
        } else if(convertView == null && xDataArrayList.get(position).getCallId()==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.loading_item, parent, false);
            convertView.setTag(VIEW_TYPE.PROG);
            return convertView;
        } else if (convertView!=null){
            if (convertView.getTag() == VIEW_TYPE.PROG && xDataArrayList.get(position).getCallId()!=null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.x_list_item, parent, false);
                convertView.setTag(VIEW_TYPE.REAL);
            } else if (convertView.getTag() == VIEW_TYPE.REAL && xDataArrayList.get(position).getCallId()==null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.loading_item, parent, false);
                convertView.setTag(VIEW_TYPE.PROG);
                return convertView;
            }
        }

        XData XData = xDataArrayList.get(position);


        TextView callFromTextView = (TextView) convertView.findViewById(R.id.xCallFromTextView);
        if (XData.getCallFrom()!=null) {
            callFromTextView.setText(XData.getCallFrom());
        }

        TextView callerNameTextView = (TextView) convertView.findViewById(R.id.xCallerNameTextView);
        if (XData.getCallerName()!=null) {
            callerNameTextView.setText(XData.getCallerName());
        }

        TextView groupNameTextView = (TextView) convertView.findViewById(R.id.xGroupNameTextView);
        if (XData.getGroupName()!=null) {
            groupNameTextView.setText(XData.getGroupName());
        }

        TextView dateTextView = (TextView) convertView.findViewById(R.id.xDateTextView);
        if (XData.getCallTime()!=null) {
            dateTextView.setText(sdfDate.format(XData.getCallTime()));
        }

        TextView timeTextView = (TextView) convertView.findViewById(R.id.xTimeTextView);
        if (XData.getCallTime()!=null) {
            timeTextView.setText(sdfTime.format(XData.getCallTime()));
        }

        TextView statusTextView = (TextView) convertView.findViewById(R.id.xStatusTextView);
        if (XData.getStatus()!=null) {
            statusTextView.setText(XData.getStatus());
        }

        return convertView;
    }

    private enum VIEW_TYPE {
        REAL,
        PROG
    }
}
