package com.mcube.app.tabs.settings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcube.app.R;
import com.mcube.app.database.DatabaseManager;


public class SettingsFragment extends Fragment {

    private TextView empNameTextView;
    private TextView businessNameTextView;
    private TextView mobileNumberTextView;
    private TextView emailTextView;
    private EditText recordLimitEditText;
    private RelativeLayout signOut;
    private Button saveButton;

    Context context;

    /*BUSINESS_NAME TEXT,
    CODE INTEGER,
    EMP_CONTACT TEXT,
    EMP_EMAIL TEXT,
    EMP_NAME TEXT,
    MESSAGE TEXT,
    RECORD_LIMIT INTEGER*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container,false);

        context = getActivity();

        empNameTextView = (TextView) view.findViewById(R.id.empNameTextView);
        businessNameTextView = (TextView) view.findViewById(R.id.businessNameTextView);
        mobileNumberTextView = (TextView) view.findViewById(R.id.mobileNumberTextView);
        emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        recordLimitEditText = (EditText) view.findViewById(R.id.recordLimitEditText);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        signOut = (RelativeLayout) view.findViewById(R.id.signOut);

        final SQLiteDatabase db = DatabaseManager.getDb(context);
        final Cursor cursor = db.query("ACCOUNT", null, null, null, null, null, null, null);
        cursor.moveToFirst();
        final int id = cursor.getInt(0);
        String empName = cursor.getString(6);
        String businessName = cursor.getString(2);
        String mobileNumber = cursor.getString(4);
        String email = cursor.getString(5);
        int recordLimit = cursor.getInt(8);

        empNameTextView.setText(empName);
        mobileNumberTextView.setText(mobileNumber);
        businessNameTextView.setText(businessName);
        emailTextView.setText(email);
        recordLimitEditText.setText(""+recordLimit);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int recordLmt = Integer.parseInt(recordLimitEditText.getText().toString());
                if (recordLmt <= 0) {
                    Toast.makeText(context, "Record Limit should be greater than 0.", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("RECORD_LIMIT", 20);
                    db.update("ACCOUNT", contentValues, cursor.getColumnName(0) + " = '" + id + "'", null);
                    Toast.makeText(context, "Settings saved successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("AUTH_KEY","null");
                contentValues.put("BUSINESS_NAME","null");
                contentValues.put("CODE",000);
                contentValues.put("EMP_CONTACT","null");
                contentValues.put("EMP_EMAIL","null");
                contentValues.put("EMP_NAME","null");
                contentValues.put("MESSAGE", "null");
                contentValues.put("RECORD_LIMIT",20);
                db.update("ACCOUNT", contentValues, cursor.getColumnName(0) + " = '" + id + "'", null);
                Toast.makeText(context, "Sign-Out successful", Toast.LENGTH_SHORT).show();
                getActivity().finish();

            }
        });


        return view;
    }
}
