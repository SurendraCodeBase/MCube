package com.mcube.app.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DeviceStatus {

    //Check if phone is online
    public static boolean onlineStatus(Context activityContext) {
        ConnectivityManager cm = (ConnectivityManager)
                activityContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] activeNetInfo = cm.getAllNetworkInfo();
        boolean isConnected = false;
        for (NetworkInfo i : activeNetInfo) {
            if (i.getState() == NetworkInfo.State.CONNECTED) {
                isConnected = true;
                break;
            }
        }
        return  isConnected;
    }

    public static int memoryRemaining (Context activityContext) {
        int memClass = ((ActivityManager) activityContext.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
        return memClass;
    }

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static int getBatteryLevel(Context context) {
        int batteryPercentage = 0;
        try {
            IntentFilter ifilter = new IntentFilter(
                    Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,
                    -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE,
                    -1);
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS,
                    -1);

            float batteryPct = level / (float) scale;
            batteryPercentage = (int) (batteryPct * 100);
            if (batteryPercentage < 0) {
                batteryPercentage = 0;
            }
            McubeUtils.infoLog("Battery level remaining : " + batteryPercentage + "%");
            String strStatus = "";
            switch (status) {
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    strStatus = "Unknown Charged";
                    break;
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    strStatus = "Charged Plugged";
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    strStatus = "Charged Unplugged";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    strStatus = "Not Charging";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    strStatus = "Charged Completed";
                    break;
            }
            McubeUtils.infoLog("Battery status  " + strStatus);
        }
        catch (Exception e) {
            McubeUtils.errorLog(e.toString());
        }

        return batteryPercentage;
    }
}
