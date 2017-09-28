package cn.liucl.debugtools.route;

import android.content.Context;

import java.io.IOException;

import cn.liucl.debugtools.Utils;
import cn.liucl.debugtools.server.ByteResponse;
import cn.liucl.debugtools.server.HttpParamsParser;
import cn.liucl.debugtools.server.JsonResponse;
import cn.liucl.debugtools.server.Response;
import cn.liucl.debugtools.server.Result;

/**
 * Created by spawn on 17-9-28.
 */

public class RouteDispatcher {

    private Context mContext;

    private RouteDispatcher(Context mContext) {
        this.mContext = mContext;
    }

    private static RouteDispatcher sRouteDispatcher;

    public static RouteDispatcher getInstance(Context mContext) {
        if (sRouteDispatcher == null) {
            synchronized (RouteDispatcher.class) {
                if (sRouteDispatcher == null) {
                    sRouteDispatcher = new RouteDispatcher(mContext);
                }
            }
        }
        return sRouteDispatcher;
    }

    public Response dispatch(HttpParamsParser.Request parse) throws IOException {
        Response response = null;
        String requestURI = parse.getRequestURI();
        String[] urlSplit = requestURI.split("/");
        if (urlSplit.length == 1 || urlSplit[1].contains("debug")) {
            return null;
        }

        // error-demo
        if (urlSplit[1].contains("error")) {
            ErrorRoute errorRoute = new ErrorRoute();
            Result process = errorRoute.process();
            response = new JsonResponse(process);
            return response;
        }

        //资源处理
        byte[] content = Utils.loadContent(requestURI, mContext.getAssets());
        if (content != null) {
            return new ByteResponse(content);
        }
        return null;
    }
}
