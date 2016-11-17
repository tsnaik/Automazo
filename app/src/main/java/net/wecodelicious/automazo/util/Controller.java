package net.wecodelicious.automazo.util;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import net.wecodelicious.automazo.constraints.Constraints;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ronak R Patel on 06-11-2016.
 */

public class Controller extends IntentService {

    public Controller(){
        super("Controller");
    }
    public Controller(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final Intent intent1 = intent;
     //   final Context context = this;

        Thread thread = new Thread(){
            @Override
            public void run() {

                Object[] params = (Object[]) intent1.getSerializableExtra("params");
                String eventname = intent1.getAction();
                if(checkConstraints(getConstraints(eventname),params)){
                    performActions(getBaseContext(),getActions(eventname),eventname,null);
                }
            }
        };
        thread.start();

    }

    public boolean checkConstraints(List<String> constarintnames, Object[] params){
        Constraints c = new Constraints();
        for (String name: constarintnames) {
            if(!(c.check(name,getBaseContext(),getParameters(name,"constraints"))))
                return false;
        }
        return true;
    }

    public boolean performActions(Context context,List<String> actionnames,String eventname, Object[] params){
        for ( String action: actionnames) {
            Intent intent2 = null;
            try {
                intent2 = new Intent(context,context.getClass().forName( "net.wecodelicious.automazo.actions.Brightness"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            intent2.setAction(eventname);
            Object[] objects = new Object[1];
            objects[0] = 255;
            intent2.putExtra("params",(Serializable)objects);
            startService(intent2);
        }

        return  true;
    }

    public List<String> getConstraints(String eventname){
      List<String> names  = new ArrayList<>();
        names.add("AutoRotate");
        return names;
    }

    public List<String> getActions(String eventname){
        List<String> names = new ArrayList<>();
        names.add("Notifications");
        return  names;
    }

    public Object[] getParameters(String name, String type){

        switch (type){
            case "constraints":
                return null;
            case "actions":
                return null;
            default:
                return null;
        }
    }

}
