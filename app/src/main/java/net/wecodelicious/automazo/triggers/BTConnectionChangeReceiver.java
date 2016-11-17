package net.wecodelicious.automazo.triggers;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import net.wecodelicious.automazo.ui.MainActivity;

/**
 * Created by Bhaskar on 12/09/02016.
 */
public class BTConnectionChangeReceiver extends BroadcastReceiver implements Configurable {

    public static boolean isEnabled = false;
    public static String devicename=null;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice bluetoothDevice;

        switch (action){
            case BluetoothDevice.ACTION_ACL_CONNECTED:
                bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(context, "Connected to "+bluetoothDevice.getName(),
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(context, "Disconnected from "+bluetoothDevice.getName(),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static void enable() {
        if (!isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), BTConnectionChangeReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            isEnabled = true;
        }
    }


    public static void disable() {
        if (isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), BTConnectionChangeReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            isEnabled = false;
        }
    }

}
