package net.wecodelicious.automazo.triggers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import net.wecodelicious.automazo.util.Controller;
import net.wecodelicious.automazo.ui.MainActivity;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Created by Bhaskar on 04/09/02016.
 */
public class HotspotChangeReceiver extends BroadcastReceiver implements Configurable {

    public static boolean isEnabled = false;


    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO handle properly
        String action = intent.getAction();
      //  LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);

        if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {

         //   Intent intent1 = new Intent();
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);

           // if (WifiManager.WIFI_STATE_ENABLED == state % 10) {
             if(state == 13){

                Intent intent1 = new Intent(context, Controller.class);
                 intent1.setAction("Hotspot Enabled");

                 Object[] objects = null;
                 intent1.putExtra("params",(Serializable)objects);
                 context.startService(intent1);
                 // Toast.makeText(context, "HOTSPOT Enabled ", Toast.LENGTH_LONG).show();

                 //if(isApOn(context)){
                //intent1.setAction("net.wecodelicious.intent.action.HOTSPOT_ENABLED");
                //localBroadcastManager.sendBroadcast(intent1);
                 //context.sendBroadcast(intent1);
            }
             else if(state == 12){
                 //Enabling
                 Toast.makeText(context, "HOTSPOT Enabling ", Toast.LENGTH_LONG).show();

                 //intent1.setAction("net.wecodelicious.intent.action.HOTSPOT_ENABLING");
                 //localBroadcastManager.sendBroadcast(intent1);
                 //context.sendBroadcast(intent1);
             }
            else if(state==11){
                 Toast.makeText(context, "HOTSPOT Disabled ", Toast.LENGTH_LONG).show();

                 //intent1.setAction("net.wecodelicious.intent.action.HOTSPOT_DISABLED");
                 //localBroadcastManager.sendBroadcast(intent1);
                 //context.sendBroadcast(intent1);
            }
            else if(state == 10){
                 //disabling
                 Toast.makeText(context, "HOTSPOT Disabling ", Toast.LENGTH_LONG).show();

                 //intent1.setAction("net.wecodelicious.intent.action.HOTSPOT_DISABLING");
                 //localBroadcastManager.sendBroadcast(intent1);
                 //context.sendBroadcast(intent1);
             }

           // Toast.makeText(context, "HOTSPOT Changed ", Toast.LENGTH_LONG).show();

        }
    }

    public static void enable() {
        if(!isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), HotspotChangeReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            isEnabled = true;
        }
    }


    public static void disable() {
        if(isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), HotspotChangeReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            isEnabled = false;
        }
    }

    public static boolean isApOn(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        }
        catch (Throwable ignored) {}
        return false;
    }
}
