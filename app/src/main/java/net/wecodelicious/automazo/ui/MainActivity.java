package net.wecodelicious.automazo.ui;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.wecodelicious.automazo.actions.Flashlight;
import net.wecodelicious.automazo.constraints.Constraints;
import net.wecodelicious.automazo.R;
import net.wecodelicious.automazo.triggers.AirplaneModeReceiver;
import net.wecodelicious.automazo.triggers.AppInstalledRemovedReceiver;
import net.wecodelicious.automazo.triggers.BTConnectionChangeReceiver;
import net.wecodelicious.automazo.triggers.BTStateChangeReceiver;
import net.wecodelicious.automazo.triggers.GpsChangeReceiver;
import net.wecodelicious.automazo.triggers.HotspotChangeReceiver;
import net.wecodelicious.automazo.triggers.RingerModeChangeReceiver;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private static PackageManager mpackageManager;
    private static Context mcontext;

    public static Context getMcontext() {
        return mcontext;
    }

    public static PackageManager getMpackageManager() {
        return mpackageManager;
    }

    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<Integer> images = new ArrayList<Integer>();

    public static boolean isF = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Intent intent2;

         intent2 = new Intent(this, Notification.class);
intent2.setAction("XYZ");
        startService(intent2);
*/
      //  Log.d("flash","main ma");

        Constraints c = new Constraints();

      /*  boolean b ;
        b =  c.check("AirplaneMode",this,null);
        Toast.makeText(this,"Airplanemode : "+b,Toast.LENGTH_LONG).show();
        b =  c.check("AutoRotate",this,null);
        Toast.makeText(this,"Autorotate : "+b,Toast.LENGTH_LONG).show();
        b =  c.check("AutoSync",this,new String[]{"contact@wecodelicious.net"});
        Toast.makeText(this,"AutoSync : "+b,Toast.LENGTH_LONG).show();

      b = c.check("BatteryLevel",this,new Object[]{"gt",20});
      Toast.makeText(this,"Battery level : "+b,Toast.LENGTH_LONG).show();


        b = c.check("Bluetooth",this,new Object[]{"enabled"});
        Toast.makeText(this,"Bluetooth : "+b,Toast.LENGTH_LONG).show();

//        b=c.check("CalenderEvent",this,new Object[]{"namestartdateenddatedescriptionlocation","xyz",new Date(50000),new Date(500000),null,null});
  //      Toast.makeText(this,"CalenderEvent : "+b,Toast.LENGTH_LONG).show();

        b= c.check("CalenderUtil",this,new Object[]{"dayofweek",1});
        Toast.makeText(this,"Sunday : "+ b ,Toast.LENGTH_LONG).show();
*/

        //DevicePolicyManager dpm = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //dpm.lockNow();
        //Constraints.My maa = new Constraints().new My();
        //maa.verify();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mpackageManager = getPackageManager();
        mcontext = this;
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        try{
        InputStream jsonStream = getResources().openRawResource(R.raw.tiles_main);
            JSONObject jsonObject = new JSONObject(convertStreamToString(jsonStream));
            JSONArray jsonTiles = jsonObject.getJSONArray("tilesmain");

            for (int i=0,m=jsonTiles.length();i<m;i++){
               JSONObject jsonTile = jsonTiles.getJSONObject(i);
                names.add(jsonTile.getString("name"));
                Integer drawableId = getResources().getIdentifier(jsonTile.getString("icon"),"drawable",getPackageName());
                images.add(drawableId);
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        mRecyclerView.setHasFixedSize(false);

        mlayoutManager = new GridLayoutManager(this,2);
        ItemOffsetDecoration itemOffsetDecoration = new ItemOffsetDecoration(this,R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemOffsetDecoration);
        mRecyclerView.setLayoutManager(mlayoutManager);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,new RecyclerItemClickListener.OnItemClickListener(){
            @Override
            public void onItemClicked(View view, int position) {
                String clickedTileText = ((TextView)view.findViewById(R.id.tile_text)).getText().toString();


                if(clickedTileText.equals(names.get(0))) {
                    try {
                        Intent i = new Intent(MainActivity.this, View_Recipe.class);
                        startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }
                else if(clickedTileText.equals(names.get(1))){
                    try{
                        Intent i = new Intent(MainActivity.this,Add_Recipe.class);
                        startActivity(i);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                else if (clickedTileText.equals(names.get(2))){
                    try {
                        Intent i = new Intent(MainActivity.this,SettingsActivity.class);
                        startActivity(i);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }

                else if (clickedTileText.equals(names.get(3))){
                    try {
                        Intent i = new Intent(MainActivity.this,View_Templates.class);
                        startActivity(i);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            }
        }));
        mAdapter = new TileAdapter(names,images);
        mRecyclerView.setAdapter(mAdapter);

        enableall();



    }

    public static void enableall(){


        if(isF) {
            AirplaneModeReceiver.enable();

            GpsChangeReceiver.enable();

            HotspotChangeReceiver.enable();
            BTStateChangeReceiver.enable();
            BTConnectionChangeReceiver.enable();
            RingerModeChangeReceiver.enable();
            AppInstalledRemovedReceiver.enable();
            isF=false;
        }
    }
    String convertStreamToString(java.io.InputStream is) {
        try {
            java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
        catch (Exception e)
        {
            Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }
    }


}


