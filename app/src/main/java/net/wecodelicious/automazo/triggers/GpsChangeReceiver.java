package net.wecodelicious.automazo.triggers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.widget.Toast;

import net.wecodelicious.automazo.ui.MainActivity;

/**
 * Created by Bhaskar on 04/09/02016.
 */
public class GpsChangeReceiver extends BroadcastReceiver implements Configurable {

    public static boolean isEnabled = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO handle properly
        if("android.location.PROVIDERS_CHANGED".equals(intent.getAction())) {
            //Intent intent1 = new Intent();
            //LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);


            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(context, "GPS Enabled ", Toast.LENGTH_LONG).show();

                //intent.setAction("net.wecodelicious.intent.action.GPS_ENABLED");
                //localBroadcastManager.sendBroadcast(intent1);
                //context.sendBroadcast(intent1);
            } else {
                Toast.makeText(context, "GPS Disabled ", Toast.LENGTH_LONG).show();

                //intent.setAction("net.wecodelicious.intent.action.GPS_DISABLED");
                //localBroadcastManager.sendBroadcast(intent1);
                //context.sendBroadcast(intent1);

            }
           // Toast.makeText(context, "GPS Changed ", Toast.LENGTH_LONG).show();
        }
    }

    public static void enable() {
        if(!isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), GpsChangeReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            isEnabled = true;
        }
    }


    public static void disable() {
        if(isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), GpsChangeReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            isEnabled = false;
        }
    }
}
