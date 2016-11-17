package net.wecodelicious.automazo.actions;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import net.wecodelicious.automazo.R;

/**
 * Created by Ronak R Patel on 08-11-2016.
 */

public class Notifications extends IntentService implements Actionable {

    public Notifications(){
        super("Controller");
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        final Intent intent1 = intent;
        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    String action = intent1.getAction();
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Event occured ")
                                    .setContentText(action);
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, mBuilder.build());

                }catch (Exception e){
                    Log.d("Thread error","Notifications ");
                    e.printStackTrace();
                }
            }
        };
        thread.start();



    }
}
