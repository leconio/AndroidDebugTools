package io.lecon.debugtools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static io.lecon.debugtools.DebugTools.TAG;

public class DebugBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: " + intent.getAction());

        if ("io.lecon.stop_server".equals(intent.getAction())) {
            DebugTools.shutDown();
        } else if ("io.lecon.dissmiss_notificaton".equals(intent.getAction())) {
            DebugTools.cleanNotification();
        }
    }

}
