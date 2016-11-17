package net.wecodelicious.automazo.triggers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.widget.Toast;

import net.wecodelicious.automazo.ui.MainActivity;

/**
 * Created by Bhaskar on 12/09/02016.
 */
public class RingerModeChangeReceiver extends BroadcastReceiver implements Configurable {

    public static boolean isEnabled = false;


    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {

            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            switch (am.getRingerMode()) {
                case AudioManager.RINGER_MODE_SILENT:
                    Toast.makeText(context, "Silent Mode", Toast.LENGTH_SHORT).show();
                    break;

                case AudioManager.RINGER_MODE_VIBRATE:
                    Toast.makeText(context, "Vibrate Mode", Toast.LENGTH_SHORT).show();
                    break;

                case AudioManager.RINGER_MODE_NORMAL:
                    Toast.makeText(context, "Normal Mode", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public static void enable() {
        if (!isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), RingerModeChangeReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            isEnabled = true;
        }
    }


    public static void disable() {
        if (isEnabled) {
            MainActivity.getMpackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.getMcontext(), RingerModeChangeReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            isEnabled = false;
        }
    }
}
