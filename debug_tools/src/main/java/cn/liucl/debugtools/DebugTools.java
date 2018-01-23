package cn.liucl.debugtools;

import android.content.Context;
import android.util.Log;

import cn.liucl.debugtools.server.ClientServer;

/**
 * Created by amitshekhar on 15/11/16.
 */

public class DebugTools {

    public static final String TAG = DebugTools.class.getSimpleName();
    private static final int DEFAULT_PORT = 8080;
    private static ClientServer clientServer;

    private DebugTools() {
        // This class in not publicly instantiable
    }

    public static void initialize(Context context) {
        int portNumber;

        try {
            portNumber = Integer.valueOf(context.getString(R.string.PORT_NUMBER));
        } catch (NumberFormatException ex) {
            Log.e(TAG, "PORT_NUMBER should be integer", ex);
            portNumber = DEFAULT_PORT;
            Log.i(TAG, "Using DefaultHandler port : " + DEFAULT_PORT);
        }

        clientServer = new ClientServer(context, portNumber);
        clientServer.start();
    }

    public static void shutDown() {
        if (clientServer != null) {
            clientServer.stop();
            clientServer = null;
        }
    }

    public static boolean isServerRunning() {
        return clientServer != null && clientServer.isRunning();
    }

}
