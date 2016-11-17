package net.wecodelicious.automazo.triggers;

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
public class AppInstalledRemovedReceiver extends BroadcastReceiver implements Configurable {
    public static boolean isEnabled = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName=intent.getData().getEncodedSchemeSpecificPart();
        String action = intent.getAction();

        switch (action){
            case Intent.ACTION_PACKAGE_ADDED:
                Toast.makeText(context,packageName + "installed",Toast.LENGTH_SHORT).show();
                break;
            case Intent.ACTION_PACKAGE_REMOVED:
                Toast.makeText(context,packageName + "removed",Toast.LENGTH_SHORT).show();

                break;

        }

    }

    public static void enable() {
        if(!isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), AppInstalledRemovedReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            isEnabled = true;
        }
    }


    public static void disable() {
        if(isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), AppInstalledRemovedReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            isEnabled = false;
        }
    }
}
