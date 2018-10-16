package cn.liucl.debugtools.disk;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class DefaultDiskHelper implements DiskHelper {

    private static final String INNDER_STORAGE = "inner";
    private static final String SDCARD_STORAGE = "sdcard";

    private Context mContext;

    public DefaultDiskHelper(Context context) {
        this.mContext = context;
    }

    @Override
    public String getFolderList(String type, String path) throws DiskException {
        if (SDCARD_STORAGE.equals(type)) {
            return getFromSDCard(path);
        } else if (INNDER_STORAGE.equals(type)) {
            return getFromInner(path);
        } else {
            return null;
        }
    }

    @Override
    public void delete(String type, String path) {

    }

    @Override
    public void rename(String type, String path, String newName) {

    }

    private String getFromInner(String path) throws DiskException {
        File file = new File(mContext.getFilesDir(), path);
        return listFiles(file).toString();
    }

    private String getFromSDCard(String path) throws DiskException {
        if (!isExternalStorageWritable()) {
            throw DiskException.NO_PERMISSION;
        }
        File file = new File(Environment.getExternalStorageDirectory().getPath(), path);
        return listFiles(file).toString();
    }

    private JSONArray listFiles(File file) throws DiskException {
        if (!file.exists()) {
            throw DiskException.FILE_NOT_FOUND;
        }

        if (!file.isDirectory()) {
            throw DiskException.NOT_A_FOLDER;
        }

        JSONArray jsonArray = new JSONArray();
        File[] files = file.listFiles();
        for (File f : files) {
            jsonArray.put(buildItemFileResp(f));
        }
        return jsonArray;
    }

    /**
     * 列出文件返回Json
     */
    private JSONObject buildItemFileResp(File file) {
        JSONObject jsonObject = new JSONObject();
        String filename;
        boolean isFolder;
        long size = 0;
        StringBuilder permission = new StringBuilder();
        filename = file.getName();
        isFolder = file.isDirectory();
        if (!isFolder) {
            size = file.length();
        }
        permission.append(file.canRead() ? "r" : "-")
                .append(file.canWrite() ? "w" : "-")
                .append(file.canExecute() ? "x" : "-");
        try {
            jsonObject.put("filename", filename);
            jsonObject.put("isFolder", isFolder);
            jsonObject.put("size", size);
            jsonObject.put("permission", permission.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


}
