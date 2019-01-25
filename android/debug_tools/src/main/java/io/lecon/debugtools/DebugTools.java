package io.lecon.debugtools;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import io.lecon.debugtools.server.ClientServer;
import io.lecon.debugtools.server.Server;
import io.lecon.debugtools.utils.Utils;


public class DebugTools {

    private static final int NOTIFICATION_ID = 458;

    public static final String TAG = DebugTools.class.getSimpleName();
    private static final int DEFAULT_PORT = 8089;
    private static Server clientServer;
    private static int portNumber;

    private static Context context;

    private DebugTools() {
        // This class in not publicly instantiable
    }

    public static void initialize(Context context) {
        DebugTools.context = context;
        try {
            portNumber = Integer.valueOf(context.getString(R.string.PORT_NUMBER));
        } catch (NumberFormatException ex) {
            Log.e(TAG, "PORT_NUMBER should be integer", ex);
            portNumber = DEFAULT_PORT;
            Log.i(TAG, "Using ActionHandler port : " + DEFAULT_PORT);
        }

        clientServer = new ClientServer(context, portNumber);
        clientServer.start();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startNotification();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void startNotification() {
        Drawable drawable = context.getResources().getDrawable(R.drawable.remark);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.remark)
                .setLargeIcon(bitmap)
                .setContentTitle("DebugServer start successful")
                .setContentText("Open a browser with" + Utils.getIP() + ":" + portNumber)
                .setSubText("android debug tools by lecon")
                .setOngoing(true);

        Notification.BigTextStyle style = new Notification.BigTextStyle();
        style.bigText("1. Connect your phone and computer with USB \n 2. Enter 'adb forward tcp:" + portNumber + " tcp:" + portNumber + "' at the computer terminal.\n3. Open your browser with 127.0.0.1:" + portNumber);
        style.setBigContentTitle("DebugServer start successful");
        builder.setStyle(style);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Intent stopIntent = new Intent("io.lecon.stop_server");
            PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, 0);

            Intent restartIntent = new Intent("io.lecon.restart_server");
            PendingIntent restartPendingIntent = PendingIntent.getBroadcast(context, 0, restartIntent, 0);


            Intent dismissIntent = new Intent("io.lecon.dissmiss_notificaton");
            PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(context, 0, dismissIntent, 0);

            Notification.Action stopAction = new Notification.Action.Builder(null, "STOP", stopPendingIntent).build();
            Notification.Action restartAction = new Notification.Action.Builder(null, "RESTART", restartPendingIntent).build();
            Notification.Action dismissAction = new Notification.Action.Builder(null, "DISMISS", dismissPendingIntent).build();

            builder.addAction(stopAction)
                    .addAction(restartAction)
                    .addAction(dismissAction);
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void cleanNotification() {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    public static void shutDown() {
        if (clientServer != null) {
            clientServer.stop();
            clientServer = null;
        }
        cleanNotification();
    }
}
