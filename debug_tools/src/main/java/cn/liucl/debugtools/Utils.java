package cn.liucl.debugtools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.wifi.WifiManager;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by spawn on 17-9-28.
 */

public class Utils {

    private Utils() {

    }

    public static String getAddressLog(Context context, int port) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        @SuppressLint("DefaultLocale")
        final String formattedIpAddress = String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
        return "Open http://" + formattedIpAddress + ":" + port + " in your browser";
    }

    public static String getMimeType(final String file) {
        String extension = getExtension(file);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    private static String getExtension(final String name) {
        String suffix = "";
        final int idx = name.lastIndexOf(".");
        if (idx > 0) {
            suffix = name.substring(idx + 1);
        }
        return suffix;
    }

    public static byte[] loadContent(String filePath, AssetManager assetManager) throws IOException {
        InputStream input = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            input = assetManager.open(filePath.substring(1));
            byte[] buffer = new byte[1024];
            int size;
            while (-1 != (size = input.read(buffer))) {
                output.write(buffer, 0, size);
            }
            output.flush();
            return output.toByteArray();
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            try {
                if (null != input) {
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
