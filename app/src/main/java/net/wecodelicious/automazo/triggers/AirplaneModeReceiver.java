package net.wecodelicious.automazo.triggers;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import net.wecodelicious.automazo.ui.MainActivity;

/**
 * Created by Bhaskar on 04/09/02016.
 */
public class AirplaneModeReceiver extends BroadcastReceiver implements Configurable {

    public static boolean isEnabled = false;

    @Override
    public void onReceive(Context context, Intent intent) {
    //TODO Handle Properly
        if("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
            //Intent intent1 = new Intent();
            //LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);

            if (isAirplaneModeOn(context)) {
                //intent1.setAction("net.wecodelicious.intent.action.AIRPLANE_MODE_ENABLED");
                Toast.makeText(context, "Airplanemode Enabled ", Toast.LENGTH_LONG).show();
              //  localBroadcastManager.sendBroadcast(intent1);
                //context.sendBroadcast(intent1);
            } else {
              //  intent1.setAction("net.wecodelicious.intent.action.AIRPLANE_MODE_DISABLED");
                Toast.makeText(context, "Airplanemode Disabled ", Toast.LENGTH_LONG).show();
                //localBroadcastManager.sendBroadcast(intent1);
//                context.sendBroadcast(intent1);

            }
            //Toast.makeText(context, "Airplanemode Changed ", Toast.LENGTH_LONG).show();
        }
   }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }


    public static void enable() {
        if(!isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), AirplaneModeReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            isEnabled = true;
        }
    }


    public static void disable() {
        if(isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), AirplaneModeReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            isEnabled = false;
        }
    }
}



