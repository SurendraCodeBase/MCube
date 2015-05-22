package com.mcube.app;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mcube.app.database.DatabaseManager;
import com.mcube.app.util.Config;
import com.mcube.app.util.DeviceStatus;
import com.mcube.app.util.McubeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ConfigurationActivity extends ActionBarActivity {

    ProgressBar progressBar;
    TextView operationMsgTextView;
    EditText userNameEditText;
    EditText passwordEditText;
    EditText serverEditText;
    TextView showHideTextView;
    Button submitButton;
    Button cancelButton;

    boolean loginProgress = false;
    boolean passwordVisiblity = false;
    boolean isListenerInitialized = false;


    Context context;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.header_blue));
        }

        context = this;
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        operationMsgTextView = (TextView) findViewById(R.id.operation_messages);
        userNameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        serverEditText = (EditText) findViewById(R.id.serverName);
        showHideTextView = (TextView) findViewById(R.id.show_password);
        handler = new Handler();
        submitButton = (Button) findViewById(R.id.submit_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginProgress) {
                    userNameEditText.setText("");
                    passwordEditText.setText("");
                    serverEditText.setText("");
                }
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isListenerInitialized) {
                    initListeners();
                    isListenerInitialized = true;
                }
                if(!loginProgress) {
                    login();
                }
            }
        });

        showHideTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passwordVisiblity) {
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordVisiblity = true;
                    showHideTextView.setText("Hide");
                } else {
                    passwordEditText.setInputType(129);
                    passwordVisiblity = false;
                    showHideTextView.setText("Show");
                }
            }
        });

    }

    private void initListeners() {
        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0) {
                    userNameEditText.setBackgroundResource(R.drawable.rounded_edittext);
                } else {
                    userNameEditText.setBackgroundResource(R.drawable.error_edittext);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0) {
                    passwordEditText.setBackgroundResource(R.drawable.rounded_edittext);
                } else {
                    passwordEditText.setBackgroundResource(R.drawable.error_edittext);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        serverEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0) {
                    serverEditText.setBackgroundResource(R.drawable.rounded_edittext);
                } else {
                    serverEditText.setBackgroundResource(R.drawable.error_edittext);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void login() {
        boolean isFormValid = true;
        final String username = userNameEditText.getText().toString();
        if("".equalsIgnoreCase(username)) {
            isFormValid = false;
            userNameEditText.setBackgroundResource(R.drawable.error_edittext);
        }
        final String password = passwordEditText.getText().toString();
        if("".equalsIgnoreCase(password)) {
            isFormValid = false;
            passwordEditText.setBackgroundResource(R.drawable.error_edittext);
        }
        final String serverName = serverEditText.getText().toString();
        if("".equalsIgnoreCase(serverName)) {
            isFormValid = false;
            serverEditText.setBackgroundResource(R.drawable.error_edittext);
        }
        if(!isFormValid) {
            operationMsgTextView.setText("Please fill the above details");
        } else {
            if(DeviceStatus.onlineStatus(context)) {
                operationMsgTextView.setText("Validating credentials. Please wait...");
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);

                new AsyncTask<Void,Void,Void>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        loginProgress = true;
                        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, Config.AUTHENTICATION_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                McubeUtils.debugLog(s);
                                loginProgress = false;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        operationMsgTextView.setText("");
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });

                                try {
                                    JSONObject response = new JSONObject(s);
                                    String code = response.getString("code");
                                    final String message = response.getString("msg");
                                    if(code.equalsIgnoreCase("200")) {

                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("AUTH_KEY",response.getString("authKey"));
                                        contentValues.put("BUSINESS_NAME",response.getString("businessName"));
                                        contentValues.put("CODE",code);
                                        contentValues.put("EMP_CONTACT",response.getString("empContact"));
                                        contentValues.put("EMP_EMAIL",response.getString("empEmail"));
                                        contentValues.put("EMP_NAME",response.getString("empName"));
                                        contentValues.put("MESSAGE", message);


                                        SQLiteDatabase db = DatabaseManager.getDb(context);
                                        Cursor cursor = db.query("ACCOUNT", null, null, null, null, null, null, null);
                                        cursor.moveToFirst();
                                        int id = cursor.getInt(0);
                                        db.update("ACCOUNT", contentValues, cursor.getColumnName(0) + " = '" + id + "'", null);

                                        Intent intent = new Intent(context, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                operationMsgTextView.setText(message);
                                            }
                                        });

                                    }
                                    
                                } catch (JSONException e) {
                                    operationMsgTextView.setText("Something went wrong");
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(final VolleyError volleyError) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        operationMsgTextView.setText(volleyError.getMessage());
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("email",username);
                                params.put("password",password);
                                params.put("url",serverName);
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
                }.execute(null,null,null);
            } else {
                operationMsgTextView.setText("No internet connection");
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
