package com.mcube.app.tabs.followup;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mcube.app.AppController;
import com.mcube.app.R;
import com.mcube.app.database.DatabaseManager;
import com.mcube.app.util.Config;
import com.mcube.app.util.McubeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class FollowUpFragment extends Fragment {

    private static final String TAG = "FollowUpFragment";
    private ListView listView;
    private ProgressBar followUpProgressBar;
    private Handler handler;
    private Context context;
    ArrayList<FollowUpData> followUpDataArrayList = new ArrayList<FollowUpData>();
    int totalCount = 0;
    int startIndex = 0;
    boolean isListLoaded = false;
    private FollowUpListAdapter followUpListAdapter;
    private volatile boolean isLoadingInProgress = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_up,container,false);

        context = getActivity();
        handler = new Handler();
        listView = (ListView) view.findViewById(R.id.follow_up_list_view);
        followUpProgressBar = (ProgressBar) view.findViewById(R.id.followUpProgressBar);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d(TAG, "firstVisibleItem = " + firstVisibleItem + " visibleItemCount = " + visibleItemCount + " totalItemCount = " + totalItemCount);
                if (!isListLoaded) {
                    return;
                }
                Log.d(TAG, "firstVisibleItem = " + firstVisibleItem + " visibleItemCount = " + visibleItemCount + " totalItemCount = " + totalItemCount);
                if (firstVisibleItem + visibleItemCount == startIndex && startIndex < totalCount) {
                    if (!isLoadingInProgress) {
                        Log.d(TAG, "fetching more data with start index = " + (startIndex));
                        isLoadingInProgress = true;
                        new FollowUpDownloadTask().execute(null, null, null);
                    } else {
                        Log.d(TAG, "Loading already in progress for start index = " + startIndex);
                    }
                } else {
                    Log.d(TAG, "Not fetching");
                }
            }
        });
        if (followUpListAdapter != null) {
            listView.setAdapter(followUpListAdapter);
        } else {
            new FollowUpDownloadTask().execute(null, null, null);
        }

        return view;
    }

    private class FollowUpDownloadTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isListLoaded) {
                followUpProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            SQLiteDatabase db = DatabaseManager.getDb(context);
            Cursor cursor = db.query("ACCOUNT", null, null, null, null, null, null, null);
            cursor.moveToFirst();
            final String authKey = cursor.getString(1);
            final int recordLimit = cursor.getInt(8);

            StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, Config.GET_LIST_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    McubeUtils.debugLog(s);
                    isLoadingInProgress = false;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            followUpProgressBar.setVisibility(View.GONE);
                        }
                    });

                    int noOfRecords = 0;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        JSONObject response = new JSONObject(s);
                        totalCount = Integer.parseInt(response.getString("count"));
                        Log.d(TAG,"total count = " + totalCount);
                        JSONArray recordsArray = response.getJSONArray("records");
                        noOfRecords = recordsArray.length();
                        Log.d(TAG,"no of records fetched starting from start index " + startIndex + " = " + noOfRecords);
                        if (followUpDataArrayList.size()>0 && followUpDataArrayList.get(followUpDataArrayList.size()-1).getCallId()==null) {
                            followUpDataArrayList.remove(followUpDataArrayList.size()-1);
                        }
                        for(int i = 0;i<recordsArray.length();i++) {
                            JSONObject record = (JSONObject) recordsArray.get(i);
                            String callId = record.getString("callid");
                            String callFrom = record.getString("callfrom");
                            String dataId = record.getString("dataid");
                            String callerName = record.getString("callername");
                            String groupName = record.getString("groupname");
                            String callTimeString = record.getString("calltime");
                            String status = record.getString("status");

                            Date callTime = null;
                            try {
                                 callTime = sdf.parse(callTimeString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            FollowUpData followUpData = new FollowUpData(callId,callFrom,dataId,callerName,groupName,callTime,status);
                            followUpDataArrayList.add(followUpData);

                        }

                        startIndex += noOfRecords;
                        if (startIndex<totalCount) {
                            followUpDataArrayList.add(new FollowUpData(null, null, null, null, null, null,null));
                        }

                        if (listView.getAdapter() == null) {
                            followUpListAdapter = new FollowUpListAdapter(context,R.layout.follow_up_list_item,followUpDataArrayList);
                            listView.setAdapter(followUpListAdapter);

                        } else {
                            ((FollowUpListAdapter)listView.getAdapter()).notifyDataSetChanged();
                        }

                        isListLoaded = true;

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError volleyError) {
                    Log.d(TAG,"volley error");
                    isLoadingInProgress = false;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            followUpProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("authKey",authKey);
                    params.put("type","followup");
                    params.put("ofset","0");
                    params.put("limit",""+recordLimit);
                    return params;
                }
            };

            jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            jsonObjRequest.setTag(AppController.TAG);
            AppController.getInstance().addToRequestQueue(jsonObjRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }
    }
}
