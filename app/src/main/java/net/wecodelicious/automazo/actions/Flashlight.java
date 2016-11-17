package net.wecodelicious.automazo.actions;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by tsnai on 17-Nov-16.
 */

public class Flashlight extends IntentService implements Actionable
{
    private CameraManager mCameraManager;
    private String mCameraId;
    private ImageButton mTorchOnOffButton;
    private Boolean isTorchOn;
    private MediaPlayer mp;

    public Flashlight()
    {
        super("Controller");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        final Intent intent1 = intent;
       // Toast.makeText(this,"flashOuter",Toast.LENGTH_LONG).show();
        Log.d("flash","flash ma");

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
             //   Toast.makeText(getBaseContext(),"flash",Toast.LENGTH_LONG).show();
                Log.d("flash","flash ma");
                try
                {


                        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                    mCameraId = mCameraManager.getCameraIdList()[0];
                        try
                        {
                            Object[] arr = (Object[]) intent1.getSerializableExtra("params");
                            switch (arr[0].toString())
                            {
                                case "on":
                                    mCameraManager.setTorchMode(mCameraId, true);
                                    break;
                                case "off":
                                    mCameraManager.setTorchMode(mCameraId, false);
                                    break;

                            }

                            //mCameraManager.setTorchMode(mCameraId, true);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                }
                catch (Exception e)
                {

                }
            }
        };
        thread.start();
    }
}
