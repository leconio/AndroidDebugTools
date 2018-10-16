package io.lecon.debugtools.route;

import android.content.Context;

import io.lecon.debugtools.disk.DefaultDiskHelper;
import io.lecon.debugtools.disk.DiskHelper;
import io.lecon.debugtools.server.HttpParamsParser;
import io.lecon.debugtools.server.Result;

/**
 * Created by spawn on 2018/1/23.
 */

public class DiskRoute implements Route {


    private final Context mContext;

    public DiskRoute(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Result process(HttpParamsParser.Request request) {
        DiskHelper helper = new DefaultDiskHelper(mContext);
        String action = request.getRequestURI().split("/")[2];
        Result result = new Result();
        try {
            result.setMessage("ok");
            result.setSuccessful(true);
            switch (action) {
                case "delete":
                    String path = request.getGetParameter("path");
                    String type = request.getGetParameter("type");
                    helper.delete(type, path);
                    break;
                case "rename":
                    path = request.getGetParameter("path");
                    String newName = request.getGetParameter("newName");
                    type = request.getGetParameter("type");
                    helper.rename(type, path, newName);
                    break;
                case "list":
                default:
                    path = request.getGetParameter("path");
                    type = request.getGetParameter("type");
                    String folderList = helper.getFolderList(type, path);
                    result.setObj(folderList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccessful(false);
        }
        return result;
    }
}
