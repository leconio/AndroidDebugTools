package io.lecon.debugtools.utils;

import android.content.res.AssetManager;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.Enumeration;

import static io.lecon.debugtools.route.RouteDispatcher.WEB_FOLDER;

/**
 * Created by spawn on 17-9-28.
 */

public class Utils {

    private Utils() {

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

    /**
     * 先压缩在写回
     */
    public static byte[] loadFileContent(String filePath) throws IOException {
        if (!new File(filePath).isDirectory()) {
            // 文件
            InputStream input = null;
            try {
                filePath = URLDecoder.decode(filePath, "UTF-8");
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                input = new BufferedInputStream(new FileInputStream(filePath));
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
        } else {
            // 目录
            try {
                ZipCompress zipCompress = new ZipCompress(filePath);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                zipCompress.zip(output);
                return output.toByteArray();
            } catch (FileNotFoundException e) {
                return null;
            }
        }
    }

    public static byte[] loadAssetContent(String filePath, AssetManager assetManager) throws IOException {
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
            return loadAssetContent(WEB_FOLDER + "/index.html",assetManager);
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

    public static String getIP() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /***
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return false;
        }
        String[] tempList = file.list();
        File temp;
        for (String t : tempList) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + t);
            } else {
                temp = new File(path + File.separator + t);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + t);// 先删除文件夹里面的文件
                delete(path + "/" + t);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /***
     * 删除文件夹
     *
     * @param folderPath 文件夹完整绝对路径
     */
    public static void delete(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            File myFilePath = new File(folderPath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
