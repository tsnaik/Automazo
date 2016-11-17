package net.wecodelicious.automazo.constraints;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SyncAdapterType;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import net.wecodelicious.automazo.ui.MainActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class Constraints {



    public boolean check(String name, Context context, Object[] params) {
        try {
            Object parent = new Constraints();
            Class innerClass = this.getClass().forName("net.wecodelicious.automazo.constraints.Constraints$" + name);
            Constructor<?> constructor = innerClass.getDeclaredConstructor(Constraints.class);//inner object must know type of outer class
            constructor.setAccessible(true);//private inner class has private default constructor
            Object child = constructor.newInstance(parent);//inner object must know about its outer object

            Method method = innerClass.getDeclaredMethod("verify", Context.class,Object[].class);
            method.setAccessible(true);//in case of unaccessible method
            Object b = method.invoke(child, context,params);
            return (boolean) b;
        } catch (ClassNotFoundException e) {
            Toast.makeText(MainActivity.getMcontext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            Toast.makeText(MainActivity.getMcontext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Toast.makeText(MainActivity.getMcontext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Toast.makeText(MainActivity.getMcontext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (InstantiationException e) {
            Toast.makeText(MainActivity.getMcontext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return false;
    }

    private class My implements Verifiable {
        public My() {
            super();
        }

        @Override
        public boolean verify(Context context,Object[] params) {
            return false;
        }
    }

    private class AirplaneMode implements Verifiable {
        public AirplaneMode() {
            super();
        }

        @Override
        @SuppressWarnings("deprecation")
        public boolean verify(Context context, Object[] params) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return Settings.System.getInt(context.getContentResolver(),
                        Settings.System.AIRPLANE_MODE_ON, 0) != 0;
            } else {
                return Settings.Global.getInt(context.getContentResolver(),
                        Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
            }

        }
    }

    private class AutoRotate implements Verifiable {
        public AutoRotate() {
            super();
        }

        @Override
        public boolean verify(Context context, Object[] params) {

            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION, 0) != 0;

        }
    }

    private class AutoSync implements Verifiable {
        private String acc_name = null;

        public AutoSync() {
            super();
        }

        @Override
        public boolean verify(Context context, Object[] params) {
            acc_name = (String) params[0].toString();
            AccountManager acm
                    = AccountManager.get(context.getApplicationContext());
            Account[] acct = null;

            SyncAdapterType[] types = ContentResolver.getSyncAdapterTypes();
            for (SyncAdapterType type : types) {
                Log.d(TAG, "--------------------");
                Log.d(TAG, type.authority + "--" + type.accountType);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                   // return TODO;
                }
                acct = acm.getAccountsByType(type.accountType);
                for (int i = 0; i < acct.length; i++) {

                    if(acct[i].name.equals(acc_name)) {
                        boolean p = ContentResolver.isSyncActive(acct[i], type.authority);
                        return p;
                        //Log.i(TAG, "account name: " + acct[i].name);
                        //Log.i(TAG, "syncable: " + String.valueOf(p));

                    }
                }
            }
            return false;
        }

    }

    private class BatteryLevel implements Verifiable{

         public BatteryLevel() {
            super();
        }

        @Override
        public boolean verify(Context context,Object[] params) {
            float current_level;
            Intent batteryIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            // Error checking that probably isn't needed but I added just in case.
            if(level == -1 || scale == -1) {
                current_level =  50.0f;
            }

            current_level = ((float)level / (float)scale) * 100.0f;
            float req_level = Float.parseFloat(params[1].toString());

//            Toast.makeText(context.getApplicationContext(),"current : "+current_level + "   req : " + req_level,Toast.LENGTH_LONG).show();
            if(params[0].toString().equals("lt")){
                return current_level<req_level;
            }
            else if(params[0].toString().equals("gt")){
                return  current_level>req_level;
            }
            else if(params[0].toString().equals("gte")){
                return current_level>=req_level;
            }
            else if(params[0].toString().equals("lte")){
                return  current_level<=req_level;
            }
            else{
                return  current_level==req_level;
            }
        }

    }

    private class Bluetooth implements Verifiable{
        boolean connected = false;

        public Bluetooth() {
            super();
        }

        public void setConnected(boolean connected) {
            this.connected = connected;
        }

        public boolean isConnected() {
            return connected;
        }

        @Override
        public boolean verify(Context context, Object[] params) {
            String action = params[0].toString();
            final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            switch (action){
                case "enabled":
                    return (bluetoothAdapter != null && bluetoothAdapter.isEnabled());

                case "disabled":
                    return !(bluetoothAdapter != null && bluetoothAdapter.isEnabled());
                case "connected":
                    final String device_name = params[1].toString();

                    BluetoothProfile.ServiceListener mProfileListener1 = new BluetoothProfile.ServiceListener() {
                        public void onServiceConnected(int profile, BluetoothProfile proxy) {
                            if (profile == BluetoothProfile.A2DP) {
                                boolean deviceConnected = false;
                                BluetoothA2dp btA2dp = (BluetoothA2dp) proxy;
                                List<BluetoothDevice> a2dpConnectedDevices = btA2dp.getConnectedDevices();
                                if (a2dpConnectedDevices.size() != 0) {
                                    for (BluetoothDevice device : a2dpConnectedDevices) {
                                        if (device.getName().contains(device_name)) {
                                            deviceConnected = true;

                                        }
                                    }
                                }
                                if (!deviceConnected) {
                                   // Toast.makeText(getActivity(), "DEVICE NOT CONNECTED", Toast.LENGTH_SHORT).show();
                                }
                                bluetoothAdapter.closeProfileProxy(BluetoothProfile.A2DP, btA2dp);
                                setConnected(deviceConnected);
                            }
                        }

                        public void onServiceDisconnected(int profile) {
                            // TODO
                        }
                    };
                    bluetoothAdapter.getProfileProxy(context, mProfileListener1, BluetoothProfile.A2DP);
                    return isConnected();
                case "disconnected":
                    final String device_name1 = params[1].toString();


                    BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
                        public void onServiceConnected(int profile, BluetoothProfile proxy) {
                            if (profile == BluetoothProfile.A2DP) {
                                boolean deviceConnected = false;
                                BluetoothA2dp btA2dp = (BluetoothA2dp) proxy;
                                List<BluetoothDevice> a2dpConnectedDevices = btA2dp.getConnectedDevices();
                                if (a2dpConnectedDevices.size() != 0) {
                                    for (BluetoothDevice device : a2dpConnectedDevices) {
                                        if (device.getName().contains(device_name1)) {
                                            deviceConnected = true;

                                        }
                                    }
                                }
                                if (!deviceConnected) {
                                    // Toast.makeText(getActivity(), "DEVICE NOT CONNECTED", Toast.LENGTH_SHORT).show();
                                }
                                bluetoothAdapter.closeProfileProxy(BluetoothProfile.A2DP, btA2dp);
                                setConnected(deviceConnected);
                            }
                        }

                        public void onServiceDisconnected(int profile) {
                            // TODO
                        }
                    };
                    bluetoothAdapter.getProfileProxy(context, mProfileListener, BluetoothProfile.A2DP);
                    return !isConnected();

                default:
                    return false;
            }
        }
    }

    private class CalenderEvent implements Verifiable{
        public CalenderEvent() {super();
        }

        public  String getDate(long milliSeconds) {
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd/MM/yyyy hh:mm:ss a");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return formatter.format(calendar.getTime());
        }

        @Override
        public boolean verify(Context context, Object[] params) {

            String action = params[0].toString();
            int[] arr  = {0,0,0,0,0};

            if(action.contains("name")) { arr[0] =1 ;}
            if(action.contains("startdate")) { arr[1] =1 ;}
            if(action.contains("enddate")) { arr[2] =1 ;}
            if(action.contains("description")) { arr[3] =1 ;}
            if(action.contains("location")){arr[4]=1;}

            Cursor cursor = context.getContentResolver()
                    .query(
                            Uri.parse("content://com.android.calendar/events"),
                            new String[] { "calendar_id", "title", "description",
                                    "dtstart", "dtend", "eventLocation" }, null,
                            null, null);
            cursor.moveToFirst();
            // fetching calendars name
            int count = cursor.getCount();

            for (int i = 0; i < count; i++) {
                int[] ret = {0,0,0,0};
                if(arr[0]==1){
                    if(!params[1].toString().isEmpty() &&  params[1].toString().equalsIgnoreCase(cursor.getString(1)) == true){
                        ret[0] = 1;
                    }
                }
                if(arr[1]==1){
                   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                    String s = simpleDateFormat.format(params[2]);  // must be in form of date
                    if(!s.isEmpty() && s.equalsIgnoreCase(getDate(Long.parseLong(cursor.getString(3))))){
                        ret[1] =1;
                    }
                }
                if(arr[2]==1){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                    String s = simpleDateFormat.format(params[3]);  // must be in form of date
                    if(!s.isEmpty() && s.equalsIgnoreCase(getDate(Long.parseLong(cursor.getString(4))))){
                        ret[2] =1;
                    }
                }
                if(arr[3]==1){
                    if(!cursor.getString(2).isEmpty() &&  cursor.getString(2).contains(params[4].toString())){
                        ret[3] =1;
                    }
                }
                if(arr[4]==1){
                    if(!cursor.getString(5).isEmpty() && cursor.getString(5).equalsIgnoreCase(params[5].toString())){
                        ret[4] = 1;
                    }
                }

                if(Arrays.equals(arr,ret)){
                    return true;
                }
            }

            return false;
        }
    }

    private class CalenderUtil implements Verifiable{
        public CalenderUtil() {super();
        }

        @Override
        public boolean verify(Context context, Object[] params) {

            String action = params[0].toString();
            Calendar calendar = Calendar.getInstance();

            switch (action){

                case "dayofweek":
                    int day = calendar.get(Calendar.DAY_OF_WEEK);
                    return (day == Integer.parseInt(params[1].toString()));

                case "month":
                    int month = calendar.get(Calendar.MONTH);
                    return (month == (Integer.parseInt(params[1].toString())-1));

                case "year":
                    int year = calendar.get(Calendar.YEAR);
                    return (year == (Integer.parseInt(params[1].toString())));

                case "dayofweekinmonth":
                    int winm = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                    return (winm == Integer.parseInt(params[1].toString()));

                case "dayofmonth":
                    int x = calendar.get(Calendar.DAY_OF_MONTH);
                    return (x == Integer.parseInt(params[1].toString()));

                case "dayofyear":
                    int y = calendar.get(Calendar.DAY_OF_YEAR);
                    return (y == Integer.parseInt(params[1].toString()));

                case "hourofday":
                    int z = calendar.get(Calendar.HOUR_OF_DAY);
                    return (z==Integer.parseInt(params[1].toString()));

                case "weekofmonth":
                    int a = calendar.get(Calendar.WEEK_OF_MONTH);
                    return (a==Integer.parseInt(params[1].toString()));

                case "weekofyear":
                    int b = calendar.get(Calendar.WEEK_OF_YEAR);
                    return (b==Integer.parseInt(params[1].toString()));

                default:
                    return false;
            }


        }
    }



    /*private class PasswordType implements Verifiable{
        private final static String PASSWORD_TYPE_KEY = "lockscreen.password_type";

        /**
         * This constant means that android using some unlock method not described here.
         * Possible new methods would be added in the future releases.

        public final static int SOMETHING_ELSE = 0;

        /**
         * Android using "None" or "Slide" unlock method. It seems there is no way to determine which method exactly used.
         * In both cases you'll get "PASSWORD_QUALITY_SOMETHING" and "LOCK_PATTERN_ENABLED" == 0.

        public final static int NONE_OR_SLIDER = 1;

        /**
         * Android using "Face Unlock" with "Pattern" as additional unlock method. Android don't allow you to select
         * "Face Unlock" without additional unlock method.

        public final static int FACE_WITH_PATTERN = 3;

        /**
         * Android using "Face Unlock" with "PIN" as additional unlock method. Android don't allow you to select
         * "Face Unlock" without additional unlock method.

        public final static int FACE_WITH_PIN = 4;

        /**
         * Android using "Face Unlock" with some additional unlock method not described here.
         * Possible new methods would be added in the future releases. Values from 5 to 8 reserved for this situation.

        public final static int FACE_WITH_SOMETHING_ELSE = 9;

        /**
         * Android using "Pattern" unlock method.

        public final static int PATTERN = 10;

        /**
         * Android using "PIN" unlock method.

        public final static int PIN = 11;

        /**
         * Android using "Password" unlock method with password containing only letters.

        public final static int PASSWORD_ALPHABETIC = 12;

        /**
         * Android using "Password" unlock method with password containing both letters and numbers.

        public final static int PASSWORD_ALPHANUMERIC = 13;

        private boolean nonEmptyFileExists(String filename)
        {
            File file = new File(filename);
            return file.exists() && file.length() > 0;
        }

        public  int getCurrent(ContentResolver contentResolver)
        {
            long mode = Settings.Secure.getLong(contentResolver, PASSWORD_TYPE_KEY,
                    DevicePolicyManager.PASSWORD_QUALITY_SOMETHING);
            if (mode == DevicePolicyManager.PASSWORD_QUALITY_SOMETHING)
            {
                if (Settings.Secure.getInt(contentResolver, Settings.Secure.LOCK_PATTERN_ENABLED, 0) == 1)
                {
                    return PATTERN;
                }
                else return NONE_OR_SLIDER;
            }
            else if (mode == DevicePolicyManager.PASSWORD_QUALITY_BIOMETRIC_WEAK)
            {
                String dataDirPath = Environment.getDataDirectory().getAbsolutePath();
                if (nonEmptyFileExists(dataDirPath + "/system/gesture.key"))
                {
                    return FACE_WITH_PATTERN;
                }
                else if (nonEmptyFileExists(dataDirPath + "/system/password.key"))
                {
                    return FACE_WITH_PIN;
                }
                else return FACE_WITH_SOMETHING_ELSE;
            }
            else if (mode == DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC)
            {
                return PASSWORD_ALPHANUMERIC;
            }
            else if (mode == DevicePolicyManager.PASSWORD_QUALITY_ALPHABETIC)
            {
                return PASSWORD_ALPHABETIC;
            }
            else if (mode == DevicePolicyManager.PASSWORD_QUALITY_NUMERIC)
            {
                return PIN;
            }
            else return SOMETHING_ELSE;
        }

        public PasswordType() {super();
        }

        @Override
        public boolean verify(Context context, Object[] params) {
            int i = getCurrent(context.getContentResolver());

            String action = params[1].toString();

            switch (action){
                case "none":
                    return (i==NONE_OR_SLIDER);
                case "any":
                    return (i!=NONE_OR_SLIDER);
                case "pattern":
                    return (i== PATTERN);
                case "pin":
                    return (i== PIN );
                case "haspattern":
                    return  (i==PATTERN || i==FACE_WITH_PATTERN);
                case "haspin":
                    return (i==PIN || i==FACE_WITH_PIN);
                case "face":
                    return (i==FACE_WITH_PIN || i==FACE_WITH_PATTERN || i==FACE_WITH_SOMETHING_ELSE);
                case "password":
                    return (i==PASSWORD_ALPHABETIC || i==PASSWORD_ALPHANUMERIC);
                case "alphabeticpassword":
                    return (i==PASSWORD_ALPHABETIC);
                case "alphanumericpassword":
                    return (i==PASSWORD_ALPHANUMERIC);
                default:
                    return false;
            }
        }
    }*/
}
