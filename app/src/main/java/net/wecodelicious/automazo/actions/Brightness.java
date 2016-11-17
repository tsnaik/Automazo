package net.wecodelicious.automazo.actions;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.media.audiofx.BassBoost;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.Window;

/**
 * Created by tsnai on 17-Nov-16.
 */

public class Brightness extends IntentService implements Actionable
{

    public Brightness()
    {
        super("Controller");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        final Intent intent1 = intent;
        // Toast.makeText(this,"flashOuter",Toast.LENGTH_LONG).show();
        Log.d("brightness"," ma");

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                //   Toast.makeText(getBaseContext(),"flash",Toast.LENGTH_LONG).show();
                Log.d("brightness","run ma");

                try
                {
                    Object[] arr = (Object[]) intent1.getSerializableExtra("params");

                    android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, Integer.parseInt(arr[0].toString()));
                    //mCameraManager.setTorchMode(mCameraId, true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        };
        thread.start();
    }
}
