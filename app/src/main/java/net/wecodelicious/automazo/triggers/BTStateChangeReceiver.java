package net.wecodelicious.automazo.triggers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import net.wecodelicious.automazo.ui.MainActivity;

/**
 * Created by Bhaskar on 11/09/02016.
 */
public class BTStateChangeReceiver extends BroadcastReceiver implements Configurable {

    public static boolean isEnabled = false;


    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    -1);

            switch(state){

                case BluetoothAdapter.STATE_OFF:
                    Toast.makeText(context,
                            "BTStateChangedBroadcastReceiver: STATE_OFF",
                            Toast.LENGTH_LONG).show();
                    break;
                case BluetoothAdapter.STATE_ON:
                    Toast.makeText(context,
                            "BTStateChangedBroadcastReceiver: STATE_ON " ,
                            Toast.LENGTH_LONG).show();
                    break;

            }
            //Toast.makeText(context,
              //      "BT state changed",
                //    Toast.LENGTH_LONG).show();
        }

    }


    public static void enable() {
        if (!isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), BTStateChangeReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            isEnabled = true;
        }
    }


    public static void disable() {
        if (isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), BTStateChangeReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            isEnabled = false;
        }
    }
}
