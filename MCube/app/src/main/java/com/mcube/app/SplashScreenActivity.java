package com.mcube.app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mcube.app.database.DatabaseManager;
import com.mcube.app.util.McubeUtils;


public class SplashScreenActivity extends ActionBarActivity {

    private static final long WAIT_TIME = 2 * 1000;
    private static final String TAG = "SplashScreen";

    boolean isLoggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SQLiteDatabase db = DatabaseManager.getDb(this);
        Cursor cursor = db.query("ACCOUNT", null, null, null, null, null, null, null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        String authKey = cursor.getString(1);
        int code = cursor.getInt(3);
        McubeUtils.debugLog("code = " + code);
        McubeUtils.debugLog("id = " + id + " authKey = " + authKey);
        if(authKey!=null && !"null".equalsIgnoreCase(authKey)) {
            isLoggedIn = true;
        }

        Thread background = new Thread() {
            public void run() {

                try {
                    sleep(WAIT_TIME);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if(!isLoggedIn) {
                                startActivity(new Intent(getApplicationContext(), ConfigurationActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                finish();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
