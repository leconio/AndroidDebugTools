package cn.liucl.debugtools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by spawn on 2018/1/24.
 */

public class Constants {

    private static Context sContext;

    public static void init(Context context){
        sContext = context;
        initHost();
        initPort();
    }

    public static String HOST;
    public static int PORT;

    private static void initPort() {
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(SettingActivity.UNITE_PAN, Context.MODE_PRIVATE);
        PORT = sharedPreferences.getInt(SettingActivity.UNITE_PAN_PORT, 8089);
    }

    private static void initHost(){
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(SettingActivity.UNITE_PAN, Context.MODE_PRIVATE);
        HOST = sharedPreferences.getString(SettingActivity.UNITE_PAN_IP, "127.0.0.1");
    }

}
