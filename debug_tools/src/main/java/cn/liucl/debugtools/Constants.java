package cn.liucl.debugtools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by spawn on 2018/1/24.
 */

public class Constants {

    public static final String HOST = getHost();
    public static final String PORT = getPORT();

    private static Context context;

    public static void init(Context context){
        Constants.context = context;
    }

    private static String getPORT() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SettingActivity.UNITE_PAN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SettingActivity.UNITE_PAN_IP, "7896");    }

    private static String getHost(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SettingActivity.UNITE_PAN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SettingActivity.UNITE_PAN_PORT, "127.0.0.1");
    }

}
